package com.stinkytooters.stinkytootersbot.service.v2.hiscore;

import com.iwebpp.crypto.TweetNaclFast;
import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.BossEntry;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.SkillEntry;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.data.BossEntryData;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.data.HiscoreEntryData;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.data.HiscoreV2Dao;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.data.SkillEntryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Named
public class HiscoreV2Service {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HiscoreV2Dao hiscoreV2Dao;

    @Inject
    public HiscoreV2Service(HiscoreV2Dao hiscoreV2Dao) {
        this.hiscoreV2Dao = Objects.requireNonNull(hiscoreV2Dao, "HiscoreV2Dao is required.");
    }

    @Transactional(readOnly = true)
    public HiscoreV2 getHiscore(Long hiscoreId) {
        try {
            HiscoreEntryData hiscoreEntryData = hiscoreV2Dao.getHiscoreById(hiscoreId);
            return convertToApi(hiscoreEntryData);
        } catch (Exception ex) {
            String message = "An unexpected error occurred while getting hiscore (%d). %s";
            message = String.format(message, hiscoreId, ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message, ex);
        }
    }

    @Transactional(readOnly = true)
    public HiscoreV2 getHiscoreNearest(Long userId, Instant timestamp) {
        try {
            HiscoreEntryData hiscoreEntryData = hiscoreV2Dao.getHiscoreNearest(userId, Timestamp.from(timestamp));
            return convertToApi(hiscoreEntryData);
        } catch (EmptyResultDataAccessException ex) {
            String message = "Could not find a hiscore near (%s) for user (%d).";
            message = String.format(message, timestamp.toString(), userId);
            logger.warn(message);
            throw new ServiceException(message, ex);
        } catch (Exception ex) {
            String message = "An unexpected error occurred while getting hiscore nearest (%s) for user (%d). %s";
            message = String.format(message, timestamp.toString(), userId, ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message, ex);
        }
    }

    @Transactional(readOnly = true)
    public List<HiscoreV2> getHiscoresForUsersDaysBack(long userId, int daysBack) {
        logger.info("Getting hiscores for user ({}) and days back ({}).", userId, daysBack);
        try {
            Instant cutoffTime = LocalDate.now().atStartOfDay(ZoneId.of("America/Chicago")).minusDays(daysBack).toInstant();
            List<HiscoreEntryData> hiscoreData = hiscoreV2Dao.getHiscoresForUserUntilCutoffTime(userId, cutoffTime);
            return convertToApi(hiscoreData)
                    .stream()
                    .sorted(Comparator.comparing(HiscoreV2::getUpdateTime))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            String message = "An unexpected error occurred while getting hiscores for user (%d) days back (%d).";
            message = String.format(message, userId, daysBack);
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional
    public HiscoreV2 insertHiscore(HiscoreV2 hiscoreV2) {
        try {
            validateHiscore(hiscoreV2);
            HiscoreEntryData hiscoreEntryData = HiscoreEntryData.from(hiscoreV2);
            hiscoreEntryData = hiscoreV2Dao.insertHiscore(hiscoreEntryData);

            Long entryId = hiscoreEntryData.getId();
            for (SkillEntry skillEntry : hiscoreV2.getSkills().values()) {
                hiscoreV2Dao.insertSkillEntry(SkillEntryData.from(entryId, skillEntry));
            }
            for (BossEntry bossEntry : hiscoreV2.getBosses().values()) {
                hiscoreV2Dao.insertBossEntry(BossEntryData.from(entryId,bossEntry));
            }

            return getHiscore(entryId);
        } catch (ServiceException ex) {
            throw ex; // already handled
        } catch (Exception ex) {
            String message = "An unexpected error occurred while saving hiscore (%s). %s";
            message = String.format(message, hiscoreV2, ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message, ex);
        }
    }

    @Transactional
    public void bulkInsertHiscoreRespectingTimestamp(List<HiscoreV2> hiscores) {
        try {
            for (HiscoreV2 hiscore : hiscores) {
                try {
                    validateHiscore(hiscore);
                } catch (ServiceException ex) {
                    logger.error("invalid hiscore {}", hiscores);

                    EnumSet<Boss> allBosses = EnumSet.allOf(Boss.class);
                    EnumSet<Boss> containedBosses = EnumSet.copyOf(hiscore.getBosses().keySet());
                    allBosses.removeAll(containedBosses);
                    logger.info("Hiscore was missing bosses {}", allBosses);

                    throw ex;
                }
            }

            // Serial insert entries to generate ids.
            int i = 0;
            for (HiscoreV2 hiscore : hiscores) {
                if (i++ % 1000 == 0) {
                    logger.info("Batch insert {} / {}", i, hiscores.size());
                }
                HiscoreEntryData data = hiscoreV2Dao.insertHiscoreRespectingTimestamp(HiscoreEntryData.from(hiscore));
                hiscore.setId(data.getId());
            }

            List<SkillEntryData> skillEntryData = hiscores.stream()
                    .map(hs -> hs.getSkills().values()
                            .stream()
                            .map(se -> SkillEntryData.from(hs.getId(), se))
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());

            List<BossEntryData> bossEntryData = hiscores.stream()
                    .map(hs -> hs.getBosses().values()
                            .stream()
                            .map(se -> BossEntryData.from(hs.getId(), se))
                            .collect(Collectors.toList()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());


            logger.info("Bulk inserting skill entries.");
            hiscoreV2Dao.bulkInsertSkillEntry(skillEntryData);
            logger.info("Bulk inserting boss entries.");
            hiscoreV2Dao.bulkInsertBossEntry(bossEntryData);
        } catch (ServiceException ex) {
            throw ex; // already handled
        } catch (Exception ex) {
            String message = "An unexpected error occurred while saving hiscore (%s). %s";
            message = String.format(message, hiscores, ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message, ex);
        }
    }

    private HiscoreV2 convertToApi(HiscoreEntryData hiscoreEntryData) {
        return convertToApi(Arrays.asList(hiscoreEntryData)).get(0);
    }

    private List<HiscoreV2> convertToApi(Collection<HiscoreEntryData> hiscoreEntryData) {
        Set<Long> hiscoreIds = hiscoreEntryData.stream()
                .map(HiscoreEntryData::getId)
                .collect(Collectors.toSet());

        Map<Long, List<SkillEntryData>> skillEntriesByHiscoreId = hiscoreV2Dao.getSkillEntriesByHiscoreIds(hiscoreIds)
                .stream()
                .collect(Collectors.groupingBy(SkillEntryData::getHiscoreEntryId));
        Map<Long, List<BossEntryData>> bossEntriesByHiscoreId = hiscoreV2Dao.getBossEntriesByHiscoreIds(hiscoreIds)
                .stream()
                .collect(Collectors.groupingBy(BossEntryData::getHiscoreEntryId));

        List<HiscoreV2> hiscores = new ArrayList<>();
        for (HiscoreEntryData hiscoreEntry : hiscoreEntryData) {
            List<SkillEntryData> skillEntries = skillEntriesByHiscoreId.get(hiscoreEntry.getId());
            List<BossEntryData> bossEntries = bossEntriesByHiscoreId.get(hiscoreEntry.getId());
            if (hiscoreEntry != null && skillEntries != null && bossEntries != null) {
                HiscoreV2 hiscoreV2 = new HiscoreV2();
                hiscoreV2.setId(hiscoreEntry.getId());
                hiscoreV2.setUserId(hiscoreEntry.getUserId());
                if (hiscoreEntry.getTimestamp() != null) {
                    hiscoreV2.setUpdateTime(hiscoreEntry.getTimestamp().toInstant());
                }

                for (SkillEntryData skillEntryData : skillEntries) {
                    hiscoreV2.addSkill(skillEntryData.toSkillEntry());
                }

                for (BossEntryData bossEntryData : bossEntries) {
                    hiscoreV2.addBoss(bossEntryData.toBossEntry());
                }

                hiscores.add(hiscoreV2);
            }
        }
        return hiscores;
    }

    private void validateHiscore(HiscoreV2 hiscoreV2) {
        if (hiscoreV2.getUserId() < 1L) {
            throw new ServiceException("User id must be >= 1.");
        }

        int numSkills = Skill.values().length;
        if (hiscoreV2.getSkills().size() != numSkills) {
            String message = String.format("Expected hiscore entry to contain (%d) skills.", numSkills);
            throw new ServiceException(message);
        }

        int numBosses = Boss.values().length;
        if (hiscoreV2.getBosses().size() != numBosses) {
            String message = String.format("Expected hiscore entry to contain (%d) bosses.", numBosses);
            throw new ServiceException(message);
        }
    }

}
