package com.stinkytooters.stinkytootersbot.service.hiscore.data;

import com.stinkytooters.stinkytootersbot.api.internal.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Named
public class HiscoreDao {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final HiscoreRowMapper hiscoreRowMapper = new HiscoreRowMapper();

    private static String SELECT_LATEST_HISCORE_BY_USERID = "select * from (" +
            " select *, row_number() over (partition by his_userid order by his_update_time desc) as rn" +
            " from %s.hiscores ) t2 where rn = 1 and his_userid = :userId";

    private static String SELECT_HISCORE_NEAREST_TIMESTAMP = "select * from %s.hiscores where his_userid = :userid and" +
            " his_update_time = (select his_update_time from %s.hiscores where his_userid = :userid order by" +
            " abs(extract(epoch from (his_update_time - :nearestTimestamp))) limit 1)";

    private static String SELECT_HISCORE_BY_USERID_UNTIL_TIME = "select * from %s.hiscores where his_userid = :userid and" +
            " his_update_time >= :cutoffTime";

    // not proud of this one
    private static String INSERT_HISCORE = "insert into %s.hiscores (his_userid, his_update_time, his_overall_xp," +
            " his_overall_rank, his_overall_level, his_attack_xp, his_attack_rank, his_attack_level, his_defence_xp," +
            " his_defence_rank, his_defence_level, his_strength_xp, his_strength_rank, his_strength_level, his_hitpoints_xp, his_hitpoints_rank," +
            " his_hitpoints_level, his_ranged_xp, his_ranged_rank, his_ranged_level, his_prayer_xp, his_prayer_rank, his_prayer_level," +
            " his_magic_xp, his_magic_rank, his_magic_level, his_cooking_xp, his_cooking_rank, his_cooking_level, his_woodcutting_xp," +
            " his_woodcutting_rank, his_woodcutting_level, his_fletching_xp, his_fletching_rank, his_fletching_level, his_fishing_xp," +
            " his_fishing_rank, his_fishing_level, his_firemaking_xp, his_firemaking_rank, his_firemaking_level, his_crafting_xp," +
            " his_crafting_rank, his_crafting_level, his_smithing_xp, his_smithing_rank, his_smithing_level, his_mining_xp," +
            " his_mining_rank, his_mining_level, his_herblore_xp, his_herblore_rank, his_herblore_level, his_agility_xp," +
            " his_agility_rank, his_agility_level, his_thieving_xp, his_thieving_rank, his_thieving_level, his_slayer_xp," +
            " his_slayer_rank, his_slayer_level, his_farming_xp, his_farming_rank, his_farming_level, his_runecraft_xp," +
            " his_runecraft_rank, his_runecraft_level, his_hunter_xp, his_hunter_rank, his_hunter_level, his_construction_xp," +
            " his_construction_rank, his_construction_level, his_gotr_rank, his_gotr_kc, his_sire_rank, his_sire_kc, his_hydra_rank," +
            " his_hyrdra_kc, his_artio_rank, his_artio_kc, his_barrows_rank, his_barrows_kc, his_bryophyta_rank, his_bryophyta_kc," +
            " his_callisto_rank, his_callisto_kc, his_calvarion_rank, his_calvarion_kc, his_cerberus_rank, his_cerberus_kc, his_cox_rank," +
            " his_cox_kc, his_coxcm_rank, his_coxcm_kc, his_ce_rank, his_ce_kc, his_cf_rank, his_cf_kc, his_zilyana_rank, his_zilyana_kc," +
            " his_corp_rank, his_corp_kc, his_ca_rank, his_ca_kc, his_prime_rank, his_prime_kc, his_rex_rank, his_rex_kc, his_supreme_rank," + 
            " his_supreme_kc, his_deranged_rank, his_deranged_kc, his_duke_rank, his_duke_kc, his_graadror_rank, his_graardor_kc, his_mole_rank," +
            " his_mole_kc, his_gg_rank, his_gg_kc, his_hespori_rank, his_hespori_kc, his_kq_rank, his_kq_kc, his_kbd_rank, his_kbd_kc, his_kraken_rank," +
            " his_kraken_kc, his_kreearra_rank, his_kreearra_kc, his_kril_rank, his_kril_kc, his_mimic_rank, his_mimic_kc, his_nex_rank, his_nex_kc," + 
            " his_nightmare_rank, his_nightmare_kc, his_phosanis_rank, his_phosanis_kc, his_obor_rank, his_obor_kc, his_muspah_rank, his_muspah_kc," +
            " his_sarachnis_rank, his_sarachnis_kc, his_scorpia_rank, his_scorpia_kc, his_skotizo_rank, his_skotizo_kc, his_spindel_rank, his_spindel_kc," + 
            " his_tempoross_rank, his_tempoross_kc, his_gauntlet_rank, his_gauntlet_kc, his_cg_rank, his_cg_kc, his_leviathan_rank, his_leviathan_kc," + 
            " his_whisperer_rank, his_whisperer_kc, his_tob_rank, his_tob_kc, his_tobhm_rank, his_tobhm_kc, his_thermy_rank, his_thermy_kc, his_toa_rank," + 
            " his_toa_kc, his_toaexpert_rank, his_toaexpert_kc, his_zuk_rank, his_zuk_kc, his_jad_rank, his_jad_kc, his_vardorvis_rank, his_vardorvis_kc," + 
            " his_venenatis_rank, his_venenatis_kc, his_vetion_rank, his_vetion_kc, his_vorkath_rank, his_vorkath_kc, his_wintertodt_rank, his_wintertodt_kc," +
            " his_zalcano_rank, his_zalcano_kc, his_zulrah_rank, his_zulrah_kc) values (:userId, current_timestamp, :overallXp, :overallRank, :overallLevel," +
            " :attackXp, :attackRank, :attackLevel, :defenceXp, :defenceRank, :defenceLevel, :strengthXp, :strengthRank," +
            " :strengthLevel, :hitpointsXp, :hitpointsRank, :hitpointsLevel, :rangedXp, :rangedRank, :rangedLevel, :prayerXp," +
            " :prayerRank, :prayerLevel, :magicXp, :magicRank, :magicLevel, :cookingXp, :cookingRank, :cookingLevel, :woodcuttingXp," +
            " :woodcuttingRank, :woodcuttingLevel, :fletchingXp, :fletchingRank, :fletchingLevel, :fishingXp, :fishingRank, :fishingLevel," +
            " :firemakingXp, :firemakingRank, :firemakingLevel, :craftingXp, :craftingRank, :craftingLevel, :smithingXp," +
            " :smithingRank, :smithingLevel, :miningXp, :miningRank, :miningLevel, :herbloreXp, :herbloreRank, :herbloreLevel," +
            " :agilityXp, :agilityRank, :agilityLevel, :thievingXp, :thievingRank, :thievingLevel, :slayerXp, :slayerRank," +
            " :slayerLevel, :farmingXp, :farmingRank, :farmingLevel, :runecraftXp, :runecraftRank, :runecraftLevel, :hunterXp, :hunterRank, :hunterLevel," +
            " :constructionXp, :constructionRank, :constructionLevel, :gotrRank, :gotrKc, :sireRank, :sireKc, :hydraRank, :hyrdraKc, :artioRank, :artioKc," +
            " :barrowsRank, :barrowsKc, :bryophytaRank, :bryophytaKc, :callistoRank, :callistoKc, :calvarionRank, :calvarionKc, :cerberusRank, :cerberusKc," +
            " :coxRank, :coxKc, :coxcmRank, :coxcmKc, :ceRank, :ceKc, :cfRank, :cfKc, :zilyanaRank, :zilyanaKc, :corpRank, :corpKc, :caRank, :caKc, :primeRank," +
            " :primeKc, :rexRank, :rexKc, :supremeRank, :supremeKc, :derangedRank, :derangedKc, :dukeRank, :dukeKc, :graadrorRank, :graardorKc, :moleRank, :moleKc," +
            " :ggRank, :ggKc, :hesporiRank, :hesporiKc, :kqRank, :kqKc, :kbdRank, :kbdKc, :krakenRank, :krakenKc, :kreearraRank, :kreearraKc, :krilRank, :krilKc,+" +
            " :mimicRank, :mimicKc, :nexRank, :nexKc, :nightmareRank, :nightmareKc, :phosanisRank, :phosanisKc, :oborRank, :oborKc, :muspahRank, :muspahKc, :sarachnisRank," +
            " :sarachnisKc, :scorpiaRank, :scorpiaKc, :skotizoRank, :skotizoKc, :spindelRank, :spindelKc, :temporossRank, :temporossKc, :gauntletRank, :gauntletKc, :cgRank, :cgKc," +
            " :leviathanRank, :leviathanKc, :whispererRank, :whispererKc, :tobRank, :tobKc, :tobhmRank, :tobhmKc, :thermyRank, :thermyKc, :toaRank, :toaKc, :toaexpertRank," +
            " :toaexpertKc, :zukRank, :zukKc, :jadRank, :jadKc, :vardorvisRank, :vardorvisKc, :venenatisRank, :venenatisKc, :vetionRank, :vetionKc, :vorkathRank, :vorkathKc," +
            " :wintertodtRank, :wintertodtKc, :zalcanoRank, :zalcanoKc, :zulrahRank, :zulrahKc)";

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Inject
    public HiscoreDao(@Value("${schema}") String schema,
                      NamedParameterJdbcTemplate namedJdbcTemplate) {

        this.namedJdbcTemplate = Objects.requireNonNull(namedJdbcTemplate, "NamedJdbcTemplate is required.");
        populateSchema(schema);

    }

    public Optional<HiscoreData> getLatestHiscoresByUserId(long userId) {
        logger.debug("Getting latest hiscore by user id: {}", userId);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", userId, Types.NUMERIC);

        try {
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(SELECT_LATEST_HISCORE_BY_USERID, parameters, hiscoreRowMapper));
        } catch (DataAccessException ex) {
            String message = String.format("Could not find user by user id (%d)", userId);
            logger.info(message);
            return Optional.empty();
        }
    }

    public Optional<HiscoreData> getHiscoreNearest(long userId, Timestamp timestamp) {
        logger.debug("Getting nearest hiscore ({}) - ({})", userId, timestamp);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userid", userId, Types.NUMERIC)
                .addValue("nearestTimestamp", timestamp, Types.TIMESTAMP);

        try {
            return Optional.ofNullable(namedJdbcTemplate.queryForObject(SELECT_HISCORE_NEAREST_TIMESTAMP, parameters, hiscoreRowMapper));
        } catch (DataAccessException ex) {
            String message = String.format("Could not find hiscore nearest (%d) - (%s)", userId, timestamp);
            logger.info(message);
            return Optional.empty();
        }
    }

    public List<HiscoreData> getHiscoresForUserUntilCutoffTime(long userId, Instant cutoffTime) {
        logger.debug("Getting hiscores for user ({}) until cutoff time ({}).", userId, cutoffTime);

        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userid", userId, Types.NUMERIC)
                .addValue("cutoffTime", Timestamp.from(cutoffTime), Types.TIMESTAMP);

        return namedJdbcTemplate.query(SELECT_HISCORE_BY_USERID_UNTIL_TIME, parameters, hiscoreRowMapper);
    }

    public void insertHiscore(HiscoreData hiscoreData) throws DaoException {
        logger.debug("Inserting hiscore: {}", hiscoreData);

        int inserted = namedJdbcTemplate.update(INSERT_HISCORE, getParameters(hiscoreData));
        if (inserted != 1) {
            String message = "Inserted (" + inserted + ") rows. Expected 1." ;
            logger.error(message);
            throw new DaoException(message);
        }
    }

    private MapSqlParameterSource getParameters(HiscoreData hiscoreData) {
        return new MapSqlParameterSource()
                .addValue("userId", hiscoreData.getUserId(), Types.NUMERIC)
                .addValue("overallXp", hiscoreData.getOverallXp(), Types.NUMERIC)
                .addValue("overallRank", hiscoreData.getOverallRank(), Types.NUMERIC)
                .addValue("overallLevel", hiscoreData.getOverallLevel(), Types.NUMERIC)
                .addValue("attackXp", hiscoreData.getAttackXp(), Types.NUMERIC)
                .addValue("attackRank", hiscoreData.getAttackRank(), Types.NUMERIC)
                .addValue("attackLevel", hiscoreData.getAttackLevel(), Types.NUMERIC)
                .addValue("defenceXp", hiscoreData.getDefenceXp(), Types.NUMERIC)
                .addValue("defenceRank", hiscoreData.getDefenceRank(), Types.NUMERIC)
                .addValue("defenceLevel", hiscoreData.getDefenceLevel(), Types.NUMERIC)
                .addValue("strengthXp", hiscoreData.getStrengthXp(), Types.NUMERIC)
                .addValue("strengthRank", hiscoreData.getStrengthRank(), Types.NUMERIC)
                .addValue("strengthLevel", hiscoreData.getStrengthLevel(), Types.NUMERIC)
                .addValue("hitpointsXp", hiscoreData.getHitpointsXp(), Types.NUMERIC)
                .addValue("hitpointsRank", hiscoreData.getHitpointsRank(), Types.NUMERIC)
                .addValue("hitpointsLevel", hiscoreData.getHitpointsLevel(), Types.NUMERIC)
                .addValue("rangedXp", hiscoreData.getRangedXp(), Types.NUMERIC)
                .addValue("rangedRank", hiscoreData.getRangedRank(), Types.NUMERIC)
                .addValue("rangedLevel", hiscoreData.getRangedLevel(), Types.NUMERIC)
                .addValue("prayerXp", hiscoreData.getPrayerXp(), Types.NUMERIC)
                .addValue("prayerRank", hiscoreData.getPrayerRank(), Types.NUMERIC)
                .addValue("prayerLevel", hiscoreData.getPrayerLevel(), Types.NUMERIC)
                .addValue("magicXp", hiscoreData.getMagicXp(), Types.NUMERIC)
                .addValue("magicRank", hiscoreData.getMagicRank(), Types.NUMERIC)
                .addValue("magicLevel", hiscoreData.getMagicLevel(), Types.NUMERIC)
                .addValue("cookingXp", hiscoreData.getCookingXp(), Types.NUMERIC)
                .addValue("cookingRank", hiscoreData.getCookingRank(), Types.NUMERIC)
                .addValue("cookingLevel", hiscoreData.getCookingLevel(), Types.NUMERIC)
                .addValue("woodcuttingXp", hiscoreData.getWoodcuttingXp(), Types.NUMERIC)
                .addValue("woodcuttingRank", hiscoreData.getWoodcuttingRank(), Types.NUMERIC)
                .addValue("woodcuttingLevel", hiscoreData.getWoodcuttingLevel(), Types.NUMERIC)
                .addValue("fletchingXp", hiscoreData.getFletchingXp(), Types.NUMERIC)
                .addValue("fletchingRank", hiscoreData.getFletchingRank(), Types.NUMERIC)
                .addValue("fletchingLevel", hiscoreData.getFletchingLevel(), Types.NUMERIC)
                .addValue("fishingXp", hiscoreData.getFishingXp(), Types.NUMERIC)
                .addValue("fishingRank", hiscoreData.getFishingRank(), Types.NUMERIC)
                .addValue("fishingLevel", hiscoreData.getFishingLevel(), Types.NUMERIC)
                .addValue("firemakingXp", hiscoreData.getFiremakingXp(), Types.NUMERIC)
                .addValue("firemakingRank", hiscoreData.getFiremakingRank(), Types.NUMERIC)
                .addValue("firemakingLevel", hiscoreData.getFiremakingLevel(), Types.NUMERIC)
                .addValue("craftingXp", hiscoreData.getCraftingXp(), Types.NUMERIC)
                .addValue("craftingRank", hiscoreData.getCraftingRank(), Types.NUMERIC)
                .addValue("craftingLevel", hiscoreData.getCraftingLevel(), Types.NUMERIC)
                .addValue("smithingXp", hiscoreData.getSmithingXp(), Types.NUMERIC)
                .addValue("smithingRank", hiscoreData.getSmithingRank(), Types.NUMERIC)
                .addValue("smithingLevel", hiscoreData.getSmithingLevel(), Types.NUMERIC)
                .addValue("miningXp", hiscoreData.getMiningXp(), Types.NUMERIC)
                .addValue("miningRank", hiscoreData.getMiningRank(), Types.NUMERIC)
                .addValue("miningLevel", hiscoreData.getMiningLevel(), Types.NUMERIC)
                .addValue("herbloreXp", hiscoreData.getHerbloreXp(), Types.NUMERIC)
                .addValue("herbloreRank", hiscoreData.getHerbloreRank(), Types.NUMERIC)
                .addValue("herbloreLevel", hiscoreData.getHerbloreLevel(), Types.NUMERIC)
                .addValue("agilityXp", hiscoreData.getAgilityXp(), Types.NUMERIC)
                .addValue("agilityRank", hiscoreData.getAgilityRank(), Types.NUMERIC)
                .addValue("agilityLevel", hiscoreData.getAgilityLevel(), Types.NUMERIC)
                .addValue("thievingXp", hiscoreData.getThievingXp(), Types.NUMERIC)
                .addValue("thievingRank", hiscoreData.getThievingRank(), Types.NUMERIC)
                .addValue("thievingLevel", hiscoreData.getThievingLevel(), Types.NUMERIC)
                .addValue("slayerXp", hiscoreData.getSlayerXp(), Types.NUMERIC)
                .addValue("slayerRank", hiscoreData.getSlayerRank(), Types.NUMERIC)
                .addValue("slayerLevel", hiscoreData.getSlayerLevel(), Types.NUMERIC)
                .addValue("farmingXp", hiscoreData.getFarmingXp(), Types.NUMERIC)
                .addValue("farmingRank", hiscoreData.getFarmingRank(), Types.NUMERIC)
                .addValue("farmingLevel", hiscoreData.getFarmingLevel(), Types.NUMERIC)
                .addValue("runecraftXp", hiscoreData.getRunecraftXp(), Types.NUMERIC)
                .addValue("runecraftRank", hiscoreData.getRunecraftRank(), Types.NUMERIC)
                .addValue("runecraftLevel", hiscoreData.getRunecraftLevel(), Types.NUMERIC)
                .addValue("hunterXp", hiscoreData.getHunterXp(), Types.NUMERIC)
                .addValue("hunterRank", hiscoreData.getHunterRank(), Types.NUMERIC)
                .addValue("hunterLevel", hiscoreData.getHunterLevel(), Types.NUMERIC)
                .addValue("constructionXp", hiscoreData.getConstructionXp(), Types.NUMERIC)
                .addValue("constructionRank", hiscoreData.getConstructionRank(), Types.NUMERIC)
                .addValue("constructionLevel", hiscoreData.getConstructionLevel(), Types.NUMERIC)
                .addValue("gotrRank", hiscoreData.getGotrRank(), Types.NUMERIC)
                .addValue("gotrKc", hiscoreData.getGotrKc(), Types.NUMERIC)
                .addValue("sireRank", hiscoreData.getSireRank(), Types.NUMERIC)
                .addValue("sireKc", hiscoreData.getSireKc(), Types.NUMERIC)
                .addValue("hydraRank", hiscoreData.getHydraRank(), Types.NUMERIC)
                .addValue("hyrdraKc", hiscoreData.getHyrdraKc(), Types.NUMERIC)
                .addValue("artioRank", hiscoreData.getArtioRank(), Types.NUMERIC)
                .addValue("artioKc", hiscoreData.getArtioKc(), Types.NUMERIC)
                .addValue("barrowsRank", hiscoreData.getBarrowsRank(), Types.NUMERIC)
                .addValue("barrowsKc", hiscoreData.getBarrowsKc(), Types.NUMERIC)
                .addValue("bryophytaRank", hiscoreData.getBryophytaRank(), Types.NUMERIC)
                .addValue("bryophytaKc", hiscoreData.getBryophytaKc(), Types.NUMERIC)
                .addValue("callistoRank", hiscoreData.getCallistoRank(), Types.NUMERIC)
                .addValue("callistoKc", hiscoreData.getCallistoKc(), Types.NUMERIC)
                .addValue("calvarionRank", hiscoreData.getCalvarionRank(), Types.NUMERIC)
                .addValue("calvarionKc", hiscoreData.getCalvarionKc(), Types.NUMERIC)
                .addValue("cerberusRank", hiscoreData.getCerberusRank(), Types.NUMERIC)
                .addValue("cerberusKc", hiscoreData.getCerberusKc(), Types.NUMERIC)
                .addValue("coxRank", hiscoreData.getCoxRank(), Types.NUMERIC)
                .addValue("coxKc", hiscoreData.getCoxKc(), Types.NUMERIC)
                .addValue("coxcmRank", hiscoreData.getCoxcmRank(), Types.NUMERIC)
                .addValue("coxcmKc", hiscoreData.getCoxcmKc(), Types.NUMERIC)
                .addValue("ceRank", hiscoreData.getCeRank(), Types.NUMERIC)
                .addValue("ceKc", hiscoreData.getCeKc(), Types.NUMERIC)
                .addValue("cfRank", hiscoreData.getCfRank(), Types.NUMERIC)
                .addValue("cfKc", hiscoreData.getCfKc(), Types.NUMERIC)
                .addValue("zilyanaRank", hiscoreData.getZilyanaRank(), Types.NUMERIC)
                .addValue("zilyanaKc", hiscoreData.getZilyanaKc(), Types.NUMERIC)
                .addValue("corpRank", hiscoreData.getCorpRank(), Types.NUMERIC)
                .addValue("corpKc", hiscoreData.getCorpKc(), Types.NUMERIC)
                .addValue("caRank", hiscoreData.getCaRank(), Types.NUMERIC)
                .addValue("caKc", hiscoreData.getCaKc(), Types.NUMERIC)
                .addValue("primeRank", hiscoreData.getPrimeRank(), Types.NUMERIC)
                .addValue("primeKc", hiscoreData.getPrimeKc(), Types.NUMERIC)
                .addValue("rexRank", hiscoreData.getRexRank(), Types.NUMERIC)
                .addValue("rexKc", hiscoreData.getRexKc(), Types.NUMERIC)
                .addValue("supremeRank", hiscoreData.getSupremeRank(), Types.NUMERIC)
                .addValue("supremeKc", hiscoreData.getSupremeKc(), Types.NUMERIC)
                .addValue("derangedRank", hiscoreData.getDerangedRank(), Types.NUMERIC)
                .addValue("derangedKc", hiscoreData.getDerangedKc(), Types.NUMERIC)
                .addValue("dukeRank", hiscoreData.getDukeRank(), Types.NUMERIC)
                .addValue("dukeKc", hiscoreData.getDukeKc(), Types.NUMERIC)
                .addValue("graadrorRank", hiscoreData.getGraadrorRank(), Types.NUMERIC)
                .addValue("graardorKc", hiscoreData.getGraardorKc(), Types.NUMERIC)
                .addValue("moleRank", hiscoreData.getMoleRank(), Types.NUMERIC)
                .addValue("moleKc", hiscoreData.getMoleKc(), Types.NUMERIC)
                .addValue("ggRank", hiscoreData.getGgRank(), Types.NUMERIC)
                .addValue("ggKc", hiscoreData.getGgKc(), Types.NUMERIC)
                .addValue("hesporiRank", hiscoreData.getHesporiRank(), Types.NUMERIC)
                .addValue("hesporiKc", hiscoreData.getHesporiKc(), Types.NUMERIC)
                .addValue("kqRank", hiscoreData.getKqRank(), Types.NUMERIC)
                .addValue("kqKc", hiscoreData.getKqKc(), Types.NUMERIC)
                .addValue("kbdRank", hiscoreData.getKbdRank(), Types.NUMERIC)
                .addValue("kbdKc", hiscoreData.getKbdKc(), Types.NUMERIC)
                .addValue("krakenRank", hiscoreData.getKrakenRank(), Types.NUMERIC)
                .addValue("krakenKc", hiscoreData.getKrakenKc(), Types.NUMERIC)
                .addValue("kreearraRank", hiscoreData.getKreearraRank(), Types.NUMERIC)
                .addValue("kreearraKc", hiscoreData.getKreearraKc(), Types.NUMERIC)
                .addValue("krilRank", hiscoreData.getKrilRank(), Types.NUMERIC)
                .addValue("krilKc", hiscoreData.getKrilKc(), Types.NUMERIC)
                .addValue("mimicRank", hiscoreData.getMimicRank(), Types.NUMERIC)
                .addValue("mimicKc", hiscoreData.getMimicKc(), Types.NUMERIC)
                .addValue("nexRank", hiscoreData.getNexRank(), Types.NUMERIC)
                .addValue("nexKc", hiscoreData.getNexKc(), Types.NUMERIC)
                .addValue("nightmareRank", hiscoreData.getNightmareRank(), Types.NUMERIC)
                .addValue("nightmareKc", hiscoreData.getNightmareKc(), Types.NUMERIC)
                .addValue("phosanisRank", hiscoreData.getPhosanisRank(), Types.NUMERIC)
                .addValue("phosanisKc", hiscoreData.getPhosanisKc(), Types.NUMERIC)
                .addValue("oborRank", hiscoreData.getOborRank(), Types.NUMERIC)
                .addValue("oborKc", hiscoreData.getOborKc(), Types.NUMERIC)
                .addValue("muspahRank", hiscoreData.getMuspahRank(), Types.NUMERIC)
                .addValue("muspahKc", hiscoreData.getMuspahKc(), Types.NUMERIC)
                .addValue("sarachnisRank", hiscoreData.getSarachnisRank(), Types.NUMERIC)
                .addValue("sarachnisKc", hiscoreData.getSarachnisKc(), Types.NUMERIC)
                .addValue("scorpiaRank", hiscoreData.getScorpiaRank(), Types.NUMERIC)
                .addValue("scorpiaKc", hiscoreData.getScorpiaKc(), Types.NUMERIC)
                .addValue("skotizoRank", hiscoreData.getSkotizoRank(), Types.NUMERIC)
                .addValue("skotizoKc", hiscoreData.getSkotizoKc(), Types.NUMERIC)
                .addValue("spindelRank", hiscoreData.getSpindelRank(), Types.NUMERIC)
                .addValue("spindelKc", hiscoreData.getSpindelKc(), Types.NUMERIC)
                .addValue("temporossRank", hiscoreData.getTemporossRank(), Types.NUMERIC)
                .addValue("temporossKc", hiscoreData.getTemporossKc(), Types.NUMERIC)
                .addValue("gauntletRank", hiscoreData.getGauntletRank(), Types.NUMERIC)
                .addValue("gauntletKc", hiscoreData.getGauntletKc(), Types.NUMERIC)
                .addValue("cgRank", hiscoreData.getCgRank(), Types.NUMERIC)
                .addValue("cgKc", hiscoreData.getCgKc(), Types.NUMERIC)
                .addValue("leviathanRank", hiscoreData.getLeviathanRank(), Types.NUMERIC)
                .addValue("leviathanKc", hiscoreData.getLeviathanKc(), Types.NUMERIC)
                .addValue("whispererRank", hiscoreData.getWhispererRank(), Types.NUMERIC)
                .addValue("whispererKc", hiscoreData.getWhispererKc(), Types.NUMERIC)
                .addValue("tobRank", hiscoreData.getTobRank(), Types.NUMERIC)
                .addValue("tobKc", hiscoreData.getTobKc(), Types.NUMERIC)
                .addValue("tobhmRank", hiscoreData.getTobhmRank(), Types.NUMERIC)
                .addValue("tobhmKc", hiscoreData.getTobhmKc(), Types.NUMERIC)
                .addValue("thermyRank", hiscoreData.getThermyRank(), Types.NUMERIC)
                .addValue("thermyKc", hiscoreData.getThermyKc(), Types.NUMERIC)
                .addValue("toaRank", hiscoreData.getToaRank(), Types.NUMERIC)
                .addValue("toaKc", hiscoreData.getToaKc(), Types.NUMERIC)
                .addValue("toaexpertRank", hiscoreData.getToaexpertRank(), Types.NUMERIC)
                .addValue("toaexpertKc", hiscoreData.getToaexpertKc(), Types.NUMERIC)
                .addValue("zukRank", hiscoreData.getZukRank(), Types.NUMERIC)
                .addValue("zukKc", hiscoreData.getZukKc(), Types.NUMERIC)
                .addValue("jadRank", hiscoreData.getJadRank(), Types.NUMERIC)
                .addValue("jadKc", hiscoreData.getJadKc(), Types.NUMERIC)
                .addValue("vardorvisRank", hiscoreData.getVardorvisRank(), Types.NUMERIC)
                .addValue("vardorvisKc", hiscoreData.getVardorvisKc(), Types.NUMERIC)
                .addValue("venenatisRank", hiscoreData.getVenenatisRank(), Types.NUMERIC)
                .addValue("venenatisKc", hiscoreData.getVenenatisKc(), Types.NUMERIC)
                .addValue("vetionRank", hiscoreData.getVetionRank(), Types.NUMERIC)
                .addValue("vetionKc", hiscoreData.getVetionKc(), Types.NUMERIC)
                .addValue("vorkathRank", hiscoreData.getVorkathRank(), Types.NUMERIC)
                .addValue("vorkathKc", hiscoreData.getVorkathKc(), Types.NUMERIC)
                .addValue("wintertodtRank", hiscoreData.getWintertodtRank(), Types.NUMERIC)
                .addValue("wintertodtKc", hiscoreData.getWintertodtKc(), Types.NUMERIC)
                .addValue("zalcanoRank", hiscoreData.getZalcanoRank(), Types.NUMERIC)
                .addValue("zalcanoKc", hiscoreData.getZalcanoKc(), Types.NUMERIC)
                .addValue("zulrahRank", hiscoreData.getZulrahRank(), Types.NUMERIC)
                .addValue("zulrahKc", hiscoreData.getZulrahKc(), Types.NUMERIC);
    }

    private static class HiscoreRowMapper implements RowMapper<HiscoreData> {

        @Override
        public HiscoreData mapRow(ResultSet rs, int rowNum) throws SQLException {
            HiscoreData data = new HiscoreData();

            data.setId(rs.getLong("his_id"));
            data.setUserId(rs.getLong("his_userid"));
            data.setUpdateTime(rs.getTimestamp("his_update_time"));
            data.setOverallXp(rs.getInt("his_overall_xp"));
            data.setOverallRank(rs.getInt("his_overall_rank"));
            data.setOverallLevel(rs.getInt("his_overall_level"));
            data.setAttackXp(rs.getInt("his_attack_xp"));
            data.setAttackRank(rs.getInt("his_attack_rank"));
            data.setAttackLevel(rs.getInt("his_attack_level"));
            data.setDefenceXp(rs.getInt("his_defence_xp"));
            data.setDefenceRank(rs.getInt("his_defence_rank"));
            data.setDefenceLevel(rs.getInt("his_defence_level"));
            data.setStrengthXp(rs.getInt("his_strength_xp"));
            data.setStrengthRank(rs.getInt("his_strength_rank"));
            data.setStrengthLevel(rs.getInt("his_strength_level"));
            data.setHitpointsXp(rs.getInt("his_hitpoints_xp"));
            data.setHitpointsRank(rs.getInt("his_hitpoints_rank"));
            data.setHitpointsLevel(rs.getInt("his_hitpoints_level"));
            data.setRangedXp(rs.getInt("his_ranged_xp"));
            data.setRangedRank(rs.getInt("his_ranged_rank"));
            data.setRangedLevel(rs.getInt("his_ranged_level"));
            data.setPrayerXp(rs.getInt("his_prayer_xp"));
            data.setPrayerRank(rs.getInt("his_prayer_rank"));
            data.setPrayerLevel(rs.getInt("his_prayer_level"));
            data.setMagicXp(rs.getInt("his_magic_xp"));
            data.setMagicRank(rs.getInt("his_magic_rank"));
            data.setMagicLevel(rs.getInt("his_magic_level"));
            data.setCookingXp(rs.getInt("his_cooking_xp"));
            data.setCookingRank(rs.getInt("his_cooking_rank"));
            data.setCookingLevel(rs.getInt("his_cooking_level"));
            data.setWoodcuttingXp(rs.getInt("his_woodcutting_xp"));
            data.setWoodcuttingRank(rs.getInt("his_woodcutting_rank"));
            data.setWoodcuttingLevel(rs.getInt("his_woodcutting_level"));
            data.setFletchingXp(rs.getInt("his_fletching_xp"));
            data.setFletchingRank(rs.getInt("his_fletching_rank"));
            data.setFletchingLevel(rs.getInt("his_fletching_level"));
            data.setFishingXp(rs.getInt("his_fishing_xp"));
            data.setFishingRank(rs.getInt("his_fishing_rank"));
            data.setFishingLevel(rs.getInt("his_fishing_level"));
            data.setFiremakingXp(rs.getInt("his_firemaking_xp"));
            data.setFiremakingRank(rs.getInt("his_firemaking_rank"));
            data.setFiremakingLevel(rs.getInt("his_firemaking_level"));
            data.setCraftingXp(rs.getInt("his_crafting_xp"));
            data.setCraftingRank(rs.getInt("his_crafting_rank"));
            data.setCraftingLevel(rs.getInt("his_crafting_level"));
            data.setSmithingXp(rs.getInt("his_smithing_xp"));
            data.setSmithingRank(rs.getInt("his_smithing_rank"));
            data.setSmithingLevel(rs.getInt("his_smithing_level"));
            data.setMiningXp(rs.getInt("his_mining_xp"));
            data.setMiningRank(rs.getInt("his_mining_rank"));
            data.setMiningLevel(rs.getInt("his_mining_level"));
            data.setHerbloreXp(rs.getInt("his_herblore_xp"));
            data.setHerbloreRank(rs.getInt("his_herblore_rank"));
            data.setHerbloreLevel(rs.getInt("his_herblore_level"));
            data.setAgilityXp(rs.getInt("his_agility_xp"));
            data.setAgilityRank(rs.getInt("his_agility_rank"));
            data.setAgilityLevel(rs.getInt("his_agility_level"));
            data.setThievingXp(rs.getInt("his_thieving_xp"));
            data.setThievingRank(rs.getInt("his_thieving_rank"));
            data.setThievingLevel(rs.getInt("his_thieving_level"));
            data.setSlayerXp(rs.getInt("his_slayer_xp"));
            data.setSlayerRank(rs.getInt("his_slayer_rank"));
            data.setSlayerLevel(rs.getInt("his_slayer_level"));
            data.setFarmingXp(rs.getInt("his_farming_xp"));
            data.setFarmingRank(rs.getInt("his_farming_rank"));
            data.setFarmingLevel(rs.getInt("his_farming_level"));
            data.setRunecraftXp(rs.getInt("his_runecraft_xp"));
            data.setRunecraftRank(rs.getInt("his_runecraft_rank"));
            data.setRunecraftLevel(rs.getInt("his_runecraft_level"));
            data.setHunterXp(rs.getInt("his_hunter_xp"));
            data.setHunterRank(rs.getInt("his_hunter_rank"));
            data.setHunterLevel(rs.getInt("his_hunter_level"));
            data.setConstructionXp(rs.getInt("his_construction_xp"));
            data.setConstructionRank(rs.getInt("his_construction_rank"));
            data.setConstructionLevel(rs.getInt("his_construction_level"));
            data.setGotrRank(rs.getInt("his_gotr_rank"));
            data.setGotrKc(rs.getInt("his_gotr_kc"));
            data.setSireRank(rs.getInt("his_sire_rank"));
            data.setSireKc(rs.getInt("his_sire_kc"));
            data.setHydraRank(rs.getInt("his_hydra_rank"));
            data.setHyrdraKc(rs.getInt("his_hyrdra_kc"));
            data.setArtioRank(rs.getInt("his_artio_rank"));
            data.setArtioKc(rs.getInt("his_artio_kc"));
            data.setBarrowsRank(rs.getInt("his_barrows_rank"));
            data.setBarrowsKc(rs.getInt("his_barrows_kc"));
            data.setBryophytaRank(rs.getInt("his_bryophyta_rank"));
            data.setBryophytaKc(rs.getInt("his_bryophyta_kc"));
            data.setCallistoRank(rs.getInt("his_callisto_rank"));
            data.setCallistoKc(rs.getInt("his_callisto_kc"));
            data.setCalvarionRank(rs.getInt("his_calvarion_rank"));
            data.setCalvarionKc(rs.getInt("his_calvarion_kc"));
            data.setCerberusRank(rs.getInt("his_cerberus_rank"));
            data.setCerberusKc(rs.getInt("his_cerberus_kc"));
            data.setCoxRank(rs.getInt("his_cox_rank"));
            data.setCoxKc(rs.getInt("his_cox_kc"));
            data.setCoxcmRank(rs.getInt("his_coxcm_rank"));
            data.setCoxcmKc(rs.getInt("his_coxcm_kc"));
            data.setCeRank(rs.getInt("his_ce_rank"));
            data.setCeKc(rs.getInt("his_ce_kc"));
            data.setCfRank(rs.getInt("his_cf_rank"));
            data.setCfKc(rs.getInt("his_cf_kc"));
            data.setZilyanaRank(rs.getInt("his_zilyana_rank"));
            data.setZilyanaKc(rs.getInt("his_zilyana_kc"));
            data.setCorpRank(rs.getInt("his_corp_rank"));
            data.setCorpKc(rs.getInt("his_corp_kc"));
            data.setCaRank(rs.getInt("his_ca_rank"));
            data.setCaKc(rs.getInt("his_ca_kc"));
            data.setPrimeRank(rs.getInt("his_prime_rank"));
            data.setPrimeKc(rs.getInt("his_prime_kc"));
            data.setRexRank(rs.getInt("his_rex_rank"));
            data.setRexKc(rs.getInt("his_rex_kc"));
            data.setSupremeRank(rs.getInt("his_supreme_rank"));
            data.setSupremeKc(rs.getInt("his_supreme_kc"));
            data.setDerangedRank(rs.getInt("his_deranged_rank"));
            data.setDerangedKc(rs.getInt("his_deranged_kc"));
            data.setDukeRank(rs.getInt("his_duke_rank"));
            data.setDukeKc(rs.getInt("his_duke_kc"));
            data.setGraadrorRank(rs.getInt("his_graadror_rank"));
            data.setGraardorKc(rs.getInt("his_graardor_kc"));
            data.setMoleRank(rs.getInt("his_mole_rank"));
            data.setMoleKc(rs.getInt("his_mole_kc"));
            data.setGgRank(rs.getInt("his_gg_rank"));
            data.setGgKc(rs.getInt("his_gg_kc"));
            data.setHesporiRank(rs.getInt("his_hespori_rank"));
            data.setHesporiKc(rs.getInt("his_hespori_kc"));
            data.setKqRank(rs.getInt("his_kq_rank"));
            data.setKqKc(rs.getInt("his_kq_kc"));
            data.setKbdRank(rs.getInt("his_kbd_rank"));
            data.setKbdKc(rs.getInt("his_kbd_kc"));
            data.setKrakenRank(rs.getInt("his_kraken_rank"));
            data.setKrakenKc(rs.getInt("his_kraken_kc"));
            data.setKreearraRank(rs.getInt("his_kreearra_rank"));
            data.setKreearraKc(rs.getInt("his_kreearra_kc"));
            data.setKrilRank(rs.getInt("his_kril_rank"));
            data.setKrilKc(rs.getInt("his_kril_kc"));
            data.setMimicRank(rs.getInt("his_mimic_rank"));
            data.setMimicKc(rs.getInt("his_mimic_kc"));
            data.setNexRank(rs.getInt("his_nex_rank"));
            data.setNexKc(rs.getInt("his_nex_kc"));
            data.setNightmareRank(rs.getInt("his_nightmare_rank"));
            data.setNightmareKc(rs.getInt("his_nightmare_kc"));
            data.setPhosanisRank(rs.getInt("his_phosanis_rank"));
            data.setPhosanisKc(rs.getInt("his_phosanis_kc"));
            data.setOborRank(rs.getInt("his_obor_rank"));
            data.setOborKc(rs.getInt("his_obor_kc"));
            data.setMuspahRank(rs.getInt("his_muspah_rank"));
            data.setMuspahKc(rs.getInt("his_muspah_kc"));
            data.setSarachnisRank(rs.getInt("his_sarachnis_rank"));
            data.setSarachnisKc(rs.getInt("his_sarachnis_kc"));
            data.setScorpiaRank(rs.getInt("his_scorpia_rank"));
            data.setScorpiaKc(rs.getInt("his_scorpia_kc"));
            data.setSkotizoRank(rs.getInt("his_skotizo_rank"));
            data.setSkotizoKc(rs.getInt("his_skotizo_kc"));
            data.setSpindelRank(rs.getInt("his_spindel_rank"));
            data.setSpindelKc(rs.getInt("his_spindel_kc"));
            data.setTemporossRank(rs.getInt("his_tempoross_rank"));
            data.setTemporossKc(rs.getInt("his_tempoross_kc"));
            data.setGauntletRank(rs.getInt("his_gauntlet_rank"));
            data.setGauntletKc(rs.getInt("his_gauntlet_kc"));
            data.setCgRank(rs.getInt("his_cg_rank"));
            data.setCgKc(rs.getInt("his_cg_kc"));
            data.setLeviathanRank(rs.getInt("his_leviathan_rank"));
            data.setLeviathanKc(rs.getInt("his_leviathan_kc"));
            data.setWhispererRank(rs.getInt("his_whisperer_rank"));
            data.setWhispererKc(rs.getInt("his_whisperer_kc"));
            data.setTobRank(rs.getInt("his_tob_rank"));
            data.setTobKc(rs.getInt("his_tob_kc"));
            data.setTobhmRank(rs.getInt("his_tobhm_rank"));
            data.setTobhmKc(rs.getInt("his_tobhm_kc"));
            data.setThermyRank(rs.getInt("his_thermy_rank"));
            data.setThermyKc(rs.getInt("his_thermy_kc"));
            data.setToaRank(rs.getInt("his_toa_rank"));
            data.setToaKc(rs.getInt("his_toa_kc"));
            data.setToaexpertRank(rs.getInt("his_toaexpert_rank"));
            data.setToaexpertKc(rs.getInt("his_toaexpert_kc"));
            data.setZukRank(rs.getInt("his_zuk_rank"));
            data.setZukKc(rs.getInt("his_zuk_kc"));
            data.setJadRank(rs.getInt("his_jad_rank"));
            data.setJadKc(rs.getInt("his_jad_kc"));
            data.setVardorvisRank(rs.getInt("his_vardorvis_rank"));
            data.setVardorvisKc(rs.getInt("his_vardorvis_kc"));
            data.setVenenatisRank(rs.getInt("his_venenatis_rank"));
            data.setVenenatisKc(rs.getInt("his_venenatis_kc"));
            data.setVetionRank(rs.getInt("his_vetion_rank"));
            data.setVetionKc(rs.getInt("his_vetion_kc"));
            data.setVorkathRank(rs.getInt("his_vorkath_rank"));
            data.setVorkathKc(rs.getInt("his_vorkath_kc"));
            data.setWintertodtRank(rs.getInt("his_wintertodt_rank"));
            data.setWintertodtKc(rs.getInt("his_wintertodt_kc"));
            data.setZalcanoRank(rs.getInt("his_zalcano_rank"));
            data.setZalcanoKc(rs.getInt("his_zalcano_kc"));
            data.setZulrahRank(rs.getInt("his_zulrah_rank"));
            data.setZulrahKc(rs.getInt("his_zulrah_kc"));

            return data;
        }
    }

    private void populateSchema(String schema) {
        SELECT_LATEST_HISCORE_BY_USERID = String.format(SELECT_LATEST_HISCORE_BY_USERID, schema);
        SELECT_HISCORE_NEAREST_TIMESTAMP = String.format(SELECT_HISCORE_NEAREST_TIMESTAMP, schema, schema);
        SELECT_HISCORE_BY_USERID_UNTIL_TIME = String.format(SELECT_HISCORE_BY_USERID_UNTIL_TIME, schema);
        INSERT_HISCORE = String.format(INSERT_HISCORE, schema);
    }

}
