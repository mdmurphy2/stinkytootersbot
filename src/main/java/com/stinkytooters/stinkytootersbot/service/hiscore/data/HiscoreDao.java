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
            " his_construction_rank, his_construction_level) values (:userId, current_timestamp, :overallXp, :overallRank, :overallLevel," +
            " :attackXp, :attackRank, :attackLevel, :defenceXp, :defenceRank, :defenceLevel, :strengthXp, :strengthRank," +
            " :strengthLevel, :hitpointsXp, :hitpointsRank, :hitpointsLevel, :rangedXp, :rangedRank, :rangedLevel, :prayerXp," +
            " :prayerRank, :prayerLevel, :magicXp, :magicRank, :magicLevel, :cookingXp, :cookingRank, :cookingLevel, :woodcuttingXp," +
            " :woodcuttingRank, :woodcuttingLevel, :fletchingXp, :fletchingRank, :fletchingLevel, :fishingXp, :fishingRank, :fishingLevel," +
            " :firemakingXp, :firemakingRank, :firemakingLevel, :craftingXp, :craftingRank, :craftingLevel, :smithingXp," +
            " :smithingRank, :smithingLevel, :miningXp, :miningRank, :miningLevel, :herbloreXp, :herbloreRank, :herbloreLevel," +
            " :agilityXp, :agilityRank, :agilityLevel, :thievingXp, :thievingRank, :thievingLevel, :slayerXp, :slayerRank," +
            " :slayerLevel, :farmingXp, :farmingRank, :farmingLevel, :runecraftXp, :runecraftRank, :runecraftLevel, :hunterXp, :hunterRank, :hunterLevel," +
            " :constructionXp, :constructionRank, :constructionLevel)";

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
                .addValue("constructionLevel", hiscoreData.getConstructionLevel(), Types.NUMERIC);
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
