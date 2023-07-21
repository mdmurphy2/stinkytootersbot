package com.stinkytooters.stinkytootersbot.service.hiscore;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.service.hiscore.data.HiscoreDao;
import com.stinkytooters.stinkytootersbot.service.hiscore.data.HiscoreData;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Named
public class HiscoreService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final HiscoreDao hiscoreDao;

    @Inject
    public HiscoreService(HiscoreDao hiscoreDao, UserService userService) {
        this.hiscoreDao = Objects.requireNonNull(hiscoreDao, "HiscoreDao is required.");
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Hiscore getLatestHiscore(long userId) {
        logger.info("Getting latest hiscore by userId ({})", userId);

        try {
            Optional<HiscoreData> hiscoreOptional = hiscoreDao.getLatestHiscoresByUserId(userId);
            if (hiscoreOptional.isEmpty()) {
                String message = String.format("Could not find hiscore by userId (%d)", userId);
                logger.info(message);
                throw new ServiceException(message);
            } else {
                return hiscoreOptional.get().toHiscore();
            }
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred while getting the latest hiscore by userId (%d)", userId);
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public List<Hiscore> getHiscoresForUserDaysBack(long userId, int daysBack)  {
        logger.info("Getting hiscores for user ({}) days back ({}).", userId, daysBack);

        try  {
            Instant cutoffTime = LocalDate.now().atStartOfDay(ZoneId.of("America/Chicago")).minusDays(daysBack).toInstant();
            List<HiscoreData> hiscoreData = hiscoreDao.getHiscoresForUserUntilCutoffTime(userId, cutoffTime);

            return hiscoreData.stream()
                    .map(HiscoreData::toHiscore)
                    .sorted(Comparator.comparing(Hiscore::getUpdateTime))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            String message = "An unexpected error occurred while getting hiscores for user (%d) days back (%d).";
            message = String.format(message, userId, daysBack);
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Hiscore getLatestHiscoreByUsername(User user) {
        logger.info("Getting latest hiscore by username ({})", user.getName());

        User retrievedUser;
        try {
            retrievedUser = userService.getUser(user);
        } catch (ServiceException ex) {
            String message = String.format("An unexpected error occurred while getting latest hiscore by username (%s):\n" + ex.getMessage(), user.getName());
            logger.warn(message, ex);
            throw new ServiceException(message);
        }

        try {
            Optional<HiscoreData> hiscoreDataOptional = hiscoreDao.getLatestHiscoresByUserId(retrievedUser.getId());
            if (hiscoreDataOptional.isEmpty()) {
                String message = String.format("Could not get hiscore by username (%s), hiscore data does not exist.", user.getName());
                logger.info(message);
                throw new ServiceException(message);
            }
            return hiscoreDataOptional.get().toHiscore();
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred while getting latest hiscore by username (%s)", user.getName());
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Hiscore insertHiscore(Hiscore hiscore) {
        logger.info("Inserting new hiscore: {}", hiscore);

        if (hiscore.getUserId() < 0L) {
            String message = "Cannot update a hiscore associated with no user. Please provide a user id.";
            logger.warn(message);
            throw new ServiceException(message);
        }

        try {
            hiscoreDao.insertHiscore(HiscoreData.from(hiscore));
            return getLatestHiscore(hiscore.getUserId());
        } catch (ServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            String message = String.format("An unexpected error occurred while updating hiscore (%s)", hiscore);
            logger.error(message, ex);
            throw new ServiceException(message);
        }
    }

    @Transactional(readOnly = true, noRollbackFor = Exception.class)
    public Hiscore getHiscoreNearest(long userId, Instant instant) {
        try {
            Optional<HiscoreData> dataOptional = hiscoreDao.getHiscoreNearest(userId, Timestamp.from(instant));
            if (dataOptional.isEmpty()) {
                String message = String.format("User (%d) didn't have a hiscore near (%s)", userId, instant);
                logger.info(message);
                throw new ServiceException(message);
            }
            return dataOptional.get().toHiscore();
        }  catch (ServiceException ex) {
            throw ex;
        }
        catch (Exception ex) {
            String message = String.format("An unexpected error occurred while getting hiscore nearest (%d) - (%s)", userId, instant);
            logger.error(message, ex);
            throw new ServiceException(message);
        }

    }
}
