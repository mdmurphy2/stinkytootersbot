package com.stinkytooters.stinkytootersbot.service.user;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.BossEntry;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.SkillEntry;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteDataEntry;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.clients.OsrsHiscoreLiteClient;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.HiscoreV2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Named
public class UserUpdateService {

    public enum HiscoreReference {
        OLD,
        NEW
    }

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final Map<HiscoreEntry, Skill> SKILLS_MAP = getMatchingSkillsMap();
    private static final Map<HiscoreEntry, Boss> BOSSES_MAP = generateMatchingBossesMap();

    private final UserService userService;
    private final HiscoreV2Service hiscoreService;
    private final HiscoreService legacyHiscoreService;
    private final OsrsHiscoreLiteClient osrsHiscoreLiteClient;

    @Inject
    public UserUpdateService(UserService userService, HiscoreV2Service hiscoreService, HiscoreService legacyHiscoreService, OsrsHiscoreLiteClient osrsHiscoreLiteClient) {
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
        this.hiscoreService = Objects.requireNonNull(hiscoreService, "HiscoreService is required.");
        this.osrsHiscoreLiteClient = Objects.requireNonNull(osrsHiscoreLiteClient, "OsrsHiscoreLiteClient is required.");
        this.legacyHiscoreService = Objects.requireNonNull(legacyHiscoreService, "LegacyHiscoreService is required.");
    }

    public Map<HiscoreReference, HiscoreV2> updateHiscoresFor(User user, Instant relativeTo) {
        logger.info("Updating hiscores for user: {}, relative to: {}", user, relativeTo);

        Map<HiscoreReference, HiscoreV2> scores = new HashMap<>();
        try  {
            User retrievedUser = userService.getUser(user);
            scores.put(HiscoreReference.OLD, getHiscoreRelativeToOrEmpty(retrievedUser, relativeTo));

            Optional<OsrsHiscoreLiteData> hiscoreDataOptional = osrsHiscoreLiteClient.getHiscoresForUser(user.getName());
            if (hiscoreDataOptional.isEmpty()) {
                String message = String.format("Cannot update user (%s). There was an issue communicating with OSRS hiscores.", user.getName());
                logger.warn(message);
                throw new ServiceException(message);
            }

            OsrsHiscoreLiteData data = hiscoreDataOptional.get();
            HiscoreV2 hiscore = buildHiscoreFromHiscoreData(data);
            hiscore.setUserId(retrievedUser.getId());

            Hiscore legacyHiscore = buildLegacyHiscoreFromHiscoreData(data);
            legacyHiscore.setUserId(retrievedUser.getId());
            legacyHiscoreService.insertHiscore(legacyHiscore);

            HiscoreV2 newHiscore = hiscoreService.insertHiscore(hiscore);
            scores.put(HiscoreReference.NEW, newHiscore);
            logger.info("Returning scores: {}", scores);

            return scores;
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred while updating hiscores for user (%s)", user.getName());
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    private HiscoreV2 buildHiscoreFromHiscoreData(OsrsHiscoreLiteData data) {
        HiscoreV2 hiscore = new HiscoreV2();
        for (HiscoreEntry hiscoreEntry : HiscoreEntry.getOrderedEntries()) {
            OsrsHiscoreLiteDataEntry entry = data.getEntry(hiscoreEntry);
            if (isHiscoreEntryValid(hiscoreEntry, entry)) {
                if (hiscoreEntry.isSkill()) {
                    SkillEntry skillEntry = new SkillEntry();
                    skillEntry.setXp(Long.valueOf(entry.getXp()));
                    skillEntry.setRank(Long.valueOf(entry.getRank()));
                    skillEntry.setLevel(Long.valueOf(entry.getLevelOrScore()));
                    skillEntry.setSkill(SKILLS_MAP.get(hiscoreEntry));
                    if (skillEntry.getSkill() != null) {
                        hiscore.addSkill(skillEntry);
                    }
                } else {
                    BossEntry bossEntry = new BossEntry();
                    bossEntry.setKillcount(Long.valueOf(entry.getLevelOrScore()));
                    bossEntry.setRank(Long.valueOf(entry.getRank()));
                    bossEntry.setBoss(BOSSES_MAP.get(hiscoreEntry));
                    if (bossEntry.getBoss() != null) {
                        hiscore.addBoss(bossEntry);
                    }
                }
            } else {
                String message = String.format("Detected that hiscores may not be valid. Found abnormal entry (%s) - (%s). Aborting update.", hiscoreEntry, entry);
                logger.error(message);
                throw new ServiceException(message);
            }
        }
        return hiscore;
    }

    private Hiscore buildLegacyHiscoreFromHiscoreData(OsrsHiscoreLiteData data) {
        Hiscore hiscore = new Hiscore();
        for (HiscoreEntry hiscoreEntry : HiscoreEntry.getOrderedEntries()) {
            OsrsHiscoreLiteDataEntry entry = data.getEntry(hiscoreEntry);
            logger.info("Hiscore entry ({}) and entry ({})", hiscoreEntry, entry);
            if (isHiscoreEntryValid(hiscoreEntry, entry)) {
                hiscore.addXp(hiscoreEntry, entry.getXp());
                hiscore.addRank(hiscoreEntry, entry.getRank());
                hiscore.addLevelOrScore(hiscoreEntry, entry.getLevelOrScore());
            } else {
                String message = String.format("Detected that hiscores may not be valid. Found abnormal entry (%s) - (%s). Aborting update.", hiscoreEntry, entry);
                logger.error(message);
                throw new ServiceException(message);
            }
        }
        return hiscore;
    }

    private HiscoreV2 getHiscoreRelativeToOrEmpty(User user, Instant instant) {
        try {
            return hiscoreService.getHiscoreNearest(user.getId(), instant);
        } catch (ServiceException ex) {
            return new HiscoreV2();
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

    private boolean isHiscoreEntryValid(HiscoreEntry hiscoreEntry, OsrsHiscoreLiteDataEntry entry) {
        // If overall xp is 0, it's probably a bad entry
        if (hiscoreEntry == HiscoreEntry.OVERALL) {
            if (entry.getXp() <= 0) {
                return false;
            }
        }
        return true;
    }


}
