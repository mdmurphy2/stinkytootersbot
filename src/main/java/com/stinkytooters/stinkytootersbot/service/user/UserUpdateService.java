package com.stinkytooters.stinkytootersbot.service.user;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteDataEntry;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.clients.OsrsHiscoreLiteClient;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
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

    private final UserService userService;
    private final HiscoreService hiscoreService;
    private final OsrsHiscoreLiteClient osrsHiscoreLiteClient;

    @Inject
    public UserUpdateService(UserService userService, HiscoreService hiscoreService, OsrsHiscoreLiteClient osrsHiscoreLiteClient) {
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
        this.hiscoreService = Objects.requireNonNull(hiscoreService, "HiscoreService is required.");
        this.osrsHiscoreLiteClient = Objects.requireNonNull(osrsHiscoreLiteClient, "OsrsHiscoreLiteClient is required.");
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Map<HiscoreReference, Hiscore> updateHiscoresFor(User user, Instant relativeTo) {
        logger.info("Updating hiscores for user: {}, relative to: {}", user, relativeTo);

        Map<HiscoreReference, Hiscore> scores = new HashMap<>();
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
            Hiscore hiscore = buildHiscoreFromHiscoreData(data);
            hiscore.setUserId(retrievedUser.getId());

            Hiscore newHiscore = hiscoreService.insertHiscore(hiscore);
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

    private Hiscore buildHiscoreFromHiscoreData(OsrsHiscoreLiteData data) {
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

    private boolean isHiscoreEntryValid(HiscoreEntry hiscoreEntry, OsrsHiscoreLiteDataEntry entry) {
        // If overall xp is 0, it's probably a bad entry
        if (hiscoreEntry == HiscoreEntry.OVERALL) {
            if (entry.getXp() <= 0) {
                return false;
            }
        }
        return true;
    }

    private Hiscore getHiscoreRelativeToOrEmpty(User user, Instant instant) {
        try {
            return hiscoreService.getHiscoreNearest(user.getId(), instant);
        } catch (ServiceException ex) {
            Hiscore hiscore = new Hiscore();
            hiscore.setUserId(user.getId());
            hiscore.setUpdateTime(Instant.now());
            for (HiscoreEntry hiscoreEntry : HiscoreEntry.values()) {
                hiscore.addXp(hiscoreEntry, 0);
                hiscore.addLevelOrScore(hiscoreEntry, 0);
                hiscore.addRank(hiscoreEntry, 0);
            }
            return hiscore;
        }
    }

}
