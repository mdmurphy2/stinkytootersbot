package com.stinkytooters.stinkytootersbot.scripts;

import com.iwebpp.crypto.TweetNaclFast;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.BossEntry;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.SkillEntry;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.HiscoreV2Service;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.data.SkillEntryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Named
public class MigrateData {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Instant BROKEN_TIMESTAMP = LocalDateTime.of(2024, 1, 26, 13, 0)
            .atZone(ZoneId.of("America/Chicago"))
            .toInstant();
    private static final Map<HiscoreEntry, Skill> SKILLS_MAP = getMatchingSkillsMap();
    private static final Map<HiscoreEntry, Boss> BOSSES_MAP = generateMatchingBossesMap();

    private final HiscoreV2Service hiscoreV2Service;
    private final HiscoreService hiscoreService;

    private final Map<Long, Long> maxZulrahKcByUserId = new HashMap<>();
    private final Map<Long, Long> maxZulrahRankByUserId = new HashMap<>();

    @Inject
    public MigrateData(HiscoreV2Service hiscoreV2Service, HiscoreService hiscoreService) {
        this.hiscoreV2Service = Objects.requireNonNull(hiscoreV2Service, "HiscoreV2Service is required.");
        this.hiscoreService = Objects.requireNonNull(hiscoreService, "HiscoreService is required.");
    }

    public void execute() {
        logger.info("Executing migrate data script.");
        List<Hiscore> hiscores = hiscoreService.getAllHiscores()
                .stream()
                .sorted(Comparator.comparing(Hiscore::getUpdateTime))
                .collect(Collectors.toList());
        logger.info("Got {} legacy hiscores.", hiscores.size());

        List<HiscoreV2> newHiscores = new ArrayList<>();
        for (Hiscore hiscore : hiscores) {
            newHiscores.add(convertOldHiscoreToNewHiscore(hiscore));
        }

        hiscoreV2Service.bulkInsertHiscoreRespectingTimestamp(newHiscores);
        logger.info("data migration done.");
    }

    private HiscoreV2 convertOldHiscoreToNewHiscore(Hiscore hiscore) {
        HiscoreV2 hiscoreV2 = new HiscoreV2();
        hiscoreV2.setUpdateTime(hiscore.getUpdateTime());
        hiscoreV2.setUserId(hiscore.getUserId());

        List<HiscoreEntry> orderedEntries = HiscoreEntry.getOrderedEntries();
        if (hiscore.getUpdateTime().isBefore(BROKEN_TIMESTAMP)) {
            for (HiscoreEntry entry : orderedEntries) {
                addEntry(entry, hiscore, hiscoreV2);
            }

            Long oldMaxKc = maxZulrahKcByUserId.getOrDefault(hiscore.getUserId(), -1L);
            Long oldMaxRank = maxZulrahRankByUserId.getOrDefault(hiscore.getUserId(), -1L);

            Long currentKc = Long.valueOf(hiscore.getLevelOrScore(HiscoreEntry.ZULRAH));
            Long currentRank = Long.valueOf(hiscore.getRank(HiscoreEntry.ZULRAH));

            if (currentKc > oldMaxKc) {
                maxZulrahKcByUserId.put(hiscore.getUserId(), oldMaxKc);
            }

            if (currentRank > oldMaxRank) {
                maxZulrahRankByUserId.put(hiscore.getUserId(), oldMaxRank);
            }
        } else {
            int scurrius = orderedEntries.indexOf(HiscoreEntry.SCURRIUS);
            for (int i = 0; i < orderedEntries.size() - 1; i++) {
                HiscoreEntry original = orderedEntries.get(i);
                HiscoreEntry update = orderedEntries.get(i + 1);
                if (i >= scurrius) {
                    addEntryAs(original, update, hiscore, hiscoreV2);
                } else {
                    addEntry(original, hiscore, hiscoreV2);
                }
            }

            // Insert
            BossEntry bossEntry = new BossEntry();
            bossEntry.setBoss(Boss.SCURRIUS);
            bossEntry.setRank(-1L);
            bossEntry.setKillcount(-1L);
            hiscoreV2.addBoss(bossEntry);

            BossEntry zulrah = new BossEntry();
            zulrah.setBoss(Boss.ZULRAH);
            zulrah.setKillcount(maxZulrahKcByUserId.getOrDefault(hiscore.getUserId(), -1L));
            zulrah.setRank(maxZulrahRankByUserId.getOrDefault(hiscore.getUserId(), -1L));
            hiscoreV2.addBoss(zulrah);
        }

        return hiscoreV2;
    }

    private void addEntry(HiscoreEntry entry, Hiscore hiscore, HiscoreV2 hiscoreV2) {
        if (entry.isSkill()) {
            SkillEntry skillEntry = new SkillEntry();
            skillEntry.setXp(Long.valueOf(hiscore.getXp(entry)));
            skillEntry.setRank(Long.valueOf(hiscore.getRank(entry)));
            skillEntry.setLevel(Long.valueOf(hiscore.getLevelOrScore(entry)));
            skillEntry.setSkill(SKILLS_MAP.get(entry));
            if (skillEntry.getSkill() != null) {
                hiscoreV2.addSkill(skillEntry);
            }
        } else {
            BossEntry bossEntry = new BossEntry();
            bossEntry.setKillcount(Long.valueOf(hiscore.getLevelOrScore(entry)));
            bossEntry.setRank(Long.valueOf(hiscore.getRank(entry)));
            bossEntry.setBoss(BOSSES_MAP.get(entry));
            if (bossEntry.getBoss() != null) {
                hiscoreV2.addBoss(bossEntry);
            }
        }
    }

    private void addEntryAs(HiscoreEntry entry, HiscoreEntry as, Hiscore hiscore, HiscoreV2 hiscoreV2) {
        if (entry.isSkill()) {
            SkillEntry skillEntry = new SkillEntry();
            skillEntry.setXp(Long.valueOf(hiscore.getXp(as)));
            skillEntry.setRank(Long.valueOf(hiscore.getRank(as)));
            skillEntry.setLevel(Long.valueOf(hiscore.getRank(as)));
            skillEntry.setSkill(SKILLS_MAP.get(entry));
            if (skillEntry.getSkill() != null) {
                hiscoreV2.addSkill(skillEntry);
            }
        } else {
            BossEntry bossEntry = new BossEntry();
            bossEntry.setKillcount(Long.valueOf(hiscore.getLevelOrScore(as)));
            bossEntry.setRank(Long.valueOf(hiscore.getRank(as)));
            bossEntry.setBoss(BOSSES_MAP.get(entry));
            if (bossEntry.getBoss() != null) {
                hiscoreV2.addBoss(bossEntry);
            }
        }
    }

    private static Map<HiscoreEntry, Skill> getMatchingSkillsMap() {
        Map<HiscoreEntry, Skill> map = new HashMap<>();
        map.put(HiscoreEntry.OVERALL, Skill.OVERALL);
        map.put(HiscoreEntry.ATTACK, Skill.ATTACK);
        map.put(HiscoreEntry.DEFENCE, Skill.DEFENCE);
        map.put(HiscoreEntry.STRENGTH, Skill.STRENGTH);
        map.put(HiscoreEntry.HITPOINTS, Skill.HITPOINTS);
        map.put(HiscoreEntry.RANGED, Skill.RANGED);
        map.put(HiscoreEntry.PRAYER, Skill.PRAYER);
        map.put(HiscoreEntry.MAGIC, Skill.MAGIC);
        map.put(HiscoreEntry.COOKING, Skill.COOKING);
        map.put(HiscoreEntry.WOODCUTTING, Skill.WOODCUTTING);
        map.put(HiscoreEntry.FLETCHING, Skill.FLETCHING);
        map.put(HiscoreEntry.FISHING, Skill.FISHING);
        map.put(HiscoreEntry.FIREMAKING, Skill.FIREMAKING);
        map.put(HiscoreEntry.CRAFTING, Skill.CRAFTING);
        map.put(HiscoreEntry.SMITHING, Skill.SMITHING);
        map.put(HiscoreEntry.MINING, Skill.MINING);
        map.put(HiscoreEntry.HERBLORE, Skill.HERBLORE);
        map.put(HiscoreEntry.AGILITY, Skill.AGILITY);
        map.put(HiscoreEntry.THIEVING, Skill.THIEVING);
        map.put(HiscoreEntry.SLAYER, Skill.SLAYER);
        map.put(HiscoreEntry.FARMING, Skill.FARMING);
        map.put(HiscoreEntry.RUNECRAFT, Skill.RUNECRAFT);
        map.put(HiscoreEntry.HUNTER, Skill.HUNTER);
        map.put(HiscoreEntry.CONSTRUCTION, Skill.CONSTRUCTION);
        return map;
    }

    private static Map<HiscoreEntry, Boss> generateMatchingBossesMap() {
        Map<HiscoreEntry, Boss> map = new HashMap<>();
        map.put(HiscoreEntry.GOTR, Boss.GOTR);
        map.put(HiscoreEntry.ABYSSAL_SIRE, Boss.ABYSSAL_SIRE);
        map.put(HiscoreEntry.ALCHEMICAL_HYDRA, Boss.ALCHEMICAL_HYDRA);
        map.put(HiscoreEntry.ARTIO, Boss.ARTIO);
        map.put(HiscoreEntry.BARROWS_CHESTS, Boss.BARROWS_CHESTS);
        map.put(HiscoreEntry.BRYOPHYTA, Boss.BRYOPHYTA);
        map.put(HiscoreEntry.CALLISTO, Boss.CALLISTO);
        map.put(HiscoreEntry.CALVARION, Boss.CALVARION);
        map.put(HiscoreEntry.CERBERUS, Boss.CERBERUS);
        map.put(HiscoreEntry.CHAMBERS_OF_XERIC, Boss.CHAMBERS_OF_XERIC);
        map.put(HiscoreEntry.CHAMBERS_OF_XERIC_CHALLENGE_MODE, Boss.CHAMBERS_OF_XERIC_CHALLENGE_MODE);
        map.put(HiscoreEntry.CHAOS_ELEMENTAL, Boss.CHAOS_ELEMENTAL);
        map.put(HiscoreEntry.CHAOS_FANATIC, Boss.CHAOS_FANATIC);
        map.put(HiscoreEntry.COMMANDER_ZILYANA, Boss.COMMANDER_ZILYANA);
        map.put(HiscoreEntry.CORPOREAL_BEAST, Boss.CORPOREAL_BEAST);
        map.put(HiscoreEntry.CRAZY_ARCHAEOLOGIST, Boss.CRAZY_ARCHAEOLOGIST);
        map.put(HiscoreEntry.DAGANNOTH_PRIME, Boss.DAGANNOTH_PRIME);
        map.put(HiscoreEntry.DAGANNOTH_REX, Boss.DAGANNOTH_REX);
        map.put(HiscoreEntry.DAGANNOTH_SUPREME, Boss.DAGANNOTH_SUPREME);
        map.put(HiscoreEntry.DERANGED_ARCHAEOLOGIST, Boss.DERANGED_ARCHAEOLOGIST);
        map.put(HiscoreEntry.DUKE_SUCELLUS, Boss.DUKE_SUCELLUS);
        map.put(HiscoreEntry.GENERAL_GRAARDOR, Boss.GENERAL_GRAARDOR);
        map.put(HiscoreEntry.GIANT_MOLE, Boss.GIANT_MOLE);
        map.put(HiscoreEntry.GROTESQUE_GUARDIANS, Boss.GROTESQUE_GUARDIANS);
        map.put(HiscoreEntry.HESPORI, Boss.HESPORI);
        map.put(HiscoreEntry.KALPHITE_QUEEN, Boss.KALPHITE_QUEEN);
        map.put(HiscoreEntry.KING_BLACK_DRAGON, Boss.KING_BLACK_DRAGON);
        map.put(HiscoreEntry.KRAKEN, Boss.KRAKEN);
        map.put(HiscoreEntry.KREEARRA, Boss.KREEARRA);
        map.put(HiscoreEntry.KRIL_TSUTSAROTH, Boss.KRIL_TSUTSAROTH);
        map.put(HiscoreEntry.MIMIC, Boss.MIMIC);
        map.put(HiscoreEntry.NEX, Boss.NEX);
        map.put(HiscoreEntry.NIGHTMARE, Boss.NIGHTMARE);
        map.put(HiscoreEntry.PHOSANIS_NIGHTMARE, Boss.PHOSANIS_NIGHTMARE);
        map.put(HiscoreEntry.OBOR, Boss.OBOR);
        map.put(HiscoreEntry.PHANTOM_MUSPAH, Boss.PHANTOM_MUSPAH);
        map.put(HiscoreEntry.SARACHNIS, Boss.SARACHNIS);
        map.put(HiscoreEntry.SCORPIA, Boss.SCORPIA);
        map.put(HiscoreEntry.SCURRIUS, Boss.SCURRIUS);
        map.put(HiscoreEntry.SKOTIZO, Boss.SKOTIZO);
        map.put(HiscoreEntry.SPINDEL, Boss.SPINDEL);
        map.put(HiscoreEntry.TEMPOROSS, Boss.TEMPOROSS);
        map.put(HiscoreEntry.THE_GAUNTLET, Boss.THE_GAUNTLET);
        map.put(HiscoreEntry.THE_CORRUPTED_GAUNTLET, Boss.THE_CORRUPTED_GAUNTLET);
        map.put(HiscoreEntry.THE_LEVIATHAN, Boss.THE_LEVIATHAN);
        map.put(HiscoreEntry.THE_WHISPERER, Boss.THE_WHISPERER);
        map.put(HiscoreEntry.THEATRE_OF_BLOOD, Boss.THEATRE_OF_BLOOD);
        map.put(HiscoreEntry.THEATRE_OF_BLOOD_HARD_MODE, Boss.THEATRE_OF_BLOOD_HARD_MODE);
        map.put(HiscoreEntry.THERMONUCLEAR_SMOKE_DEVIL, Boss.THERMONUCLEAR_SMOKE_DEVIL);
        map.put(HiscoreEntry.TOMBS_OF_AMASCUT, Boss.TOMBS_OF_AMASCUT);
        map.put(HiscoreEntry.TOMBS_OF_AMASCUT_EXPERT, Boss.TOMBS_OF_AMASCUT_EXPERT);
        map.put(HiscoreEntry.TZKAL_ZUK, Boss.TZKAL_ZUK);
        map.put(HiscoreEntry.TZTOK_JAD, Boss.TZTOK_JAD);
        map.put(HiscoreEntry.VARDORVIS, Boss.VARDORVIS);
        map.put(HiscoreEntry.VENENATIS, Boss.VENENATIS);
        map.put(HiscoreEntry.VETION, Boss.VETION);
        map.put(HiscoreEntry.VORKATH, Boss.VORKATH);
        map.put(HiscoreEntry.WINTERTODT, Boss.WINTERTODT);
        map.put(HiscoreEntry.ZALCANO, Boss.ZALCANO);
        map.put(HiscoreEntry.ZULRAH, Boss.ZULRAH);
        return map;
    }

}
