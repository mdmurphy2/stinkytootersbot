package com.stinkytooters.stinkytootersbot.service.v2.hiscore.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.inject.Inject;
import javax.inject.Named;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Named
public class HiscoreV2Dao {

    private static String INSERT_HISCORE_ENTRY = "insert into %s.hiscore_entry (hse_id, hse_usr_id, hse_created_timestamp)" +
            " values (nextval('%s.seq_hse_id'), :userId, current_timestamp)";
    private static String INSERT_HISCORE_ENTRY_TIMESTAMP = "insert into %s.hiscore_entry (hse_id, hse_usr_id, hse_created_timestamp)" +
            " values (nextval('%s.seq_hse_id'), :userId, :timestamp)";
    private static String INSERT_SKILL_ENTRY = "insert into %s.hiscore_skill_entry (hskl_id, hskl_hse_id, hskl_skl_id," +
            " hskl_xp, hskl_rank, hskl_level) values (nextval('%s.seq_hskl_id'), :recordId, :skillId, :xp, :rank, :level);";
    private static String INSERT_BOSS_ENTRY = "insert into %s.hiscore_boss_entry (hbos_id, hbos_hse_id, hbos_bos_id," +
            " hbos_killcount, hbos_rank) values (nextval('%s.seq_hbos_id'), :recordId, :bossId, :killcount, :rank);";

    private static String SELECT_HISCORE_BY_ID = "select * from %s.hiscore_entry where hse_id = :id";
    private static String SELECT_HISCORE_NEAREST_TIMESTAMP = "select * from %s.hiscore_entry where hse_usr_id = :userid" +
            " and hse_created_timestamp = (select hse_created_timestamp from %s.hiscore_entry where hse_usr_id = :userid" +
            " order by abs(extract(epoch from (hse_created_timestamp - :nearestTimestamp))) limit 1)";
    private static String SELECT_HISCORE_BY_USER_ID_UNTIL_TIME = "select * from %s.hiscore_entry where " +
            " hse_usr_id = :userId and hse_created_timestamp >= :cutoffTime";
    private static String SELECT_SKILL_ENTRIES_BY_HISCORE_IDS = "select * from %s.hiscore_skill_entry where hskl_hse_id" +
            " in (:hiscoreIds)";
    private static String SELECT_BOSS_ENTRIES_BY_HISCORE_IDS = "select * from %s.hiscore_boss_entry where hbos_hse_id" +
            " in (:hiscoreIds)";

    private static final RowMapper<HiscoreEntryData> HISCORE_ENTRY_ROW_MAPPER = new HiscoreEntryRowMapper();
    private static final RowMapper<SkillEntryData> SKILL_ENTRY_ROW_MAPPER = new SkillEntryRowMapper();
    private static final RowMapper<BossEntryData> BOSS_ENTRY_ROW_MAPPER = new BossEntryRowMapper();

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Inject
    public HiscoreV2Dao(NamedParameterJdbcTemplate jdbcTemplate, @Value("${database.schema}") String schema) {
        this.jdbcTemplate = Objects.requireNonNull(jdbcTemplate, "JdbcTemplate is required.");
        populateSchema(schema);
    }

    public HiscoreEntryData getHiscoreById(long id) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", id, Types.NUMERIC);
        return jdbcTemplate.queryForObject(SELECT_HISCORE_BY_ID, parameters, HISCORE_ENTRY_ROW_MAPPER);
    }

    public HiscoreEntryData getHiscoreNearest(long userId, Timestamp timestamp) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userid", userId, Types.NUMERIC)
                .addValue("nearestTimestamp", timestamp, Types.TIMESTAMP);
        return jdbcTemplate.queryForObject(SELECT_HISCORE_NEAREST_TIMESTAMP, parameters, HISCORE_ENTRY_ROW_MAPPER);
    }

    public List<SkillEntryData> getSkillEntriesByHiscoreIds(Set<Long> hiscoreIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("hiscoreIds", hiscoreIds, Types.NUMERIC);
        return jdbcTemplate.query(SELECT_SKILL_ENTRIES_BY_HISCORE_IDS, parameters, SKILL_ENTRY_ROW_MAPPER);
    }

    public List<BossEntryData> getBossEntriesByHiscoreIds(Set<Long> hiscoreIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("hiscoreIds", hiscoreIds, Types.NUMERIC);
        return jdbcTemplate.query(SELECT_BOSS_ENTRIES_BY_HISCORE_IDS, parameters, BOSS_ENTRY_ROW_MAPPER);
    }

    public List<HiscoreEntryData> getHiscoresForUserUntilCutoffTime(long userId, Instant cutoffTime) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", userId, Types.NUMERIC)
                .addValue("cutoffTime", Timestamp.from(cutoffTime), Types.TIMESTAMP);
        return jdbcTemplate.query(SELECT_HISCORE_BY_USER_ID_UNTIL_TIME, parameters, HISCORE_ENTRY_ROW_MAPPER);
    }

    public HiscoreEntryData insertHiscore(HiscoreEntryData hiscoreEntryData) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", hiscoreEntryData.getUserId(), Types.NUMERIC);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_HISCORE_ENTRY, parameters, keyHolder, new String[] { "hse_id" });
        hiscoreEntryData.setId(keyHolder.getKey().longValue());
        return hiscoreEntryData;
    }

    public HiscoreEntryData insertHiscoreRespectingTimestamp(HiscoreEntryData hiscoreEntryData) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("userId", hiscoreEntryData.getUserId(), Types.NUMERIC)
                .addValue("timestamp", hiscoreEntryData.getTimestamp(), Types.TIMESTAMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_HISCORE_ENTRY_TIMESTAMP, parameters, keyHolder, new String[] { "hse_id" });
        hiscoreEntryData.setId(keyHolder.getKey().longValue());
        return hiscoreEntryData;
    }

    public SkillEntryData insertSkillEntry(SkillEntryData skillEntryData) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("recordId", skillEntryData.getHiscoreEntryId(), Types.NUMERIC)
                .addValue("skillId", skillEntryData.getSkillId(), Types.NUMERIC)
                .addValue("xp", skillEntryData.getXp(), Types.NUMERIC)
                .addValue("rank", skillEntryData.getRank(), Types.NUMERIC)
                .addValue("level", skillEntryData.getLevel(), Types.NUMERIC);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_SKILL_ENTRY, parameters, keyHolder, new String[] { "hskl_id" });
        skillEntryData.setId(keyHolder.getKey().longValue());
        return skillEntryData;
    }

    public void bulkInsertSkillEntry(List<SkillEntryData> skillEntryDatum) {
        MapSqlParameterSource[] parameters = skillEntryDatum.stream()
                .map(skillEntryData -> new MapSqlParameterSource()
                        .addValue("recordId", skillEntryData.getHiscoreEntryId(), Types.NUMERIC)
                        .addValue("skillId", skillEntryData.getSkillId(), Types.NUMERIC)
                        .addValue("xp", skillEntryData.getXp(), Types.NUMERIC)
                        .addValue("rank", skillEntryData.getRank(), Types.NUMERIC)
                        .addValue("level", skillEntryData.getLevel(), Types.NUMERIC))
            .toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(INSERT_SKILL_ENTRY, parameters);
    }

    public void bulkInsertBossEntry(List<BossEntryData> bossEntryDatum) {
        MapSqlParameterSource[] parameters = bossEntryDatum.stream()
                .map(bossEntryData -> new MapSqlParameterSource()
                        .addValue("recordId", bossEntryData.getHiscoreEntryId(), Types.NUMERIC)
                        .addValue("bossId", bossEntryData.getBossId(), Types.NUMERIC)
                        .addValue("killcount", bossEntryData.getKillcount(), Types.NUMERIC)
                        .addValue("rank", bossEntryData.getRank(), Types.NUMERIC)
                ).toArray(MapSqlParameterSource[]::new);

        jdbcTemplate.batchUpdate(INSERT_BOSS_ENTRY, parameters);
    }

    public BossEntryData insertBossEntry(BossEntryData bossEntryData) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("recordId", bossEntryData.getHiscoreEntryId(), Types.NUMERIC)
                .addValue("bossId", bossEntryData.getBossId(), Types.NUMERIC)
                .addValue("killcount", bossEntryData.getKillcount(), Types.NUMERIC)
                .addValue("rank", bossEntryData.getRank(), Types.NUMERIC);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(INSERT_BOSS_ENTRY, parameters, keyHolder, new String[] { "hbos_id" });
        bossEntryData.setId(keyHolder.getKey().longValue());
        return bossEntryData;
    }

    private void populateSchema(String schema) {
        INSERT_HISCORE_ENTRY = String.format(INSERT_HISCORE_ENTRY, schema, schema);
        INSERT_HISCORE_ENTRY_TIMESTAMP = String.format(INSERT_HISCORE_ENTRY_TIMESTAMP, schema, schema);
        INSERT_SKILL_ENTRY = String.format(INSERT_SKILL_ENTRY, schema, schema);
        INSERT_BOSS_ENTRY = String.format(INSERT_BOSS_ENTRY, schema, schema);
        SELECT_HISCORE_BY_ID = String.format(SELECT_HISCORE_BY_ID, schema);
        SELECT_HISCORE_NEAREST_TIMESTAMP = String.format(SELECT_HISCORE_NEAREST_TIMESTAMP, schema, schema);
        SELECT_HISCORE_BY_USER_ID_UNTIL_TIME = String.format(SELECT_HISCORE_BY_USER_ID_UNTIL_TIME, schema);
        SELECT_SKILL_ENTRIES_BY_HISCORE_IDS = String.format(SELECT_SKILL_ENTRIES_BY_HISCORE_IDS, schema);
        SELECT_BOSS_ENTRIES_BY_HISCORE_IDS = String.format(SELECT_BOSS_ENTRIES_BY_HISCORE_IDS, schema);
    }

    private static class HiscoreEntryRowMapper implements RowMapper<HiscoreEntryData> {
        @Override
        public HiscoreEntryData mapRow(ResultSet rs, int rowNum) throws SQLException {
            HiscoreEntryData hiscoreEntryData = new HiscoreEntryData();
            hiscoreEntryData.setId(rs.getLong("hse_id"));
            hiscoreEntryData.setUserId(rs.getLong("hse_usr_id"));
            hiscoreEntryData.setTimestamp(rs.getTimestamp("hse_created_timestamp"));
            return hiscoreEntryData;
        }
    }

    private static class SkillEntryRowMapper implements  RowMapper<SkillEntryData> {
        @Override
        public SkillEntryData mapRow(ResultSet rs, int rowNum) throws SQLException {
            SkillEntryData skillEntryData = new SkillEntryData();
            skillEntryData.setId(rs.getLong("hskl_id"));
            skillEntryData.setHiscoreEntryId(rs.getLong("hskl_hse_id"));
            skillEntryData.setSkillId(rs.getLong("hskl_skl_id"));
            skillEntryData.setRank(rs.getLong("hskl_rank"));
            skillEntryData.setLevel(rs.getLong("hskl_level"));
            skillEntryData.setXp(rs.getLong("hskl_xp"));
            return skillEntryData;
        }
    }

    private static class BossEntryRowMapper implements RowMapper<BossEntryData> {
        @Override
        public BossEntryData mapRow(ResultSet rs, int rowNum) throws SQLException {
            BossEntryData bossEntryData = new BossEntryData();
            bossEntryData.setId(rs.getLong("hbos_id"));
            bossEntryData.setHiscoreEntryId(rs.getLong("hbos_hse_id"));
            bossEntryData.setBossId(rs.getLong("hbos_bos_id"));
            bossEntryData.setRank(rs.getLong("hbos_rank"));
            bossEntryData.setKillcount(rs.getLong("hbos_killcount"));
            return bossEntryData;
        }
    }

}
