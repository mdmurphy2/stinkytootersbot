package com.stinkytooters.stinkytootersbot.display.user;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserBuilder;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.Skill;
import com.stinkytooters.stinkytootersbot.clients.OsrsHiscoreLiteClient;
import com.stinkytooters.stinkytootersbot.display.beans.HiscoreDisplayBean;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService.HiscoreReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Named
public class UserDisplayService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String GENERIC_ERROR = "Oops! Something unexpected happened! The previous command failed, please try again.";
    private static final String ADD_USER_USAGE = "Invalid syntax. Usage: !useradd <runescape username>";
    private static final String UPDATE_USER_USAGE = "Invalid syntax. Usage: !userupdate <runescape username> [-h|-d] <amount>";

    private static final String USER_ALREADY_TRACKED_TEMPLATE = "User (%s) is already being tracked.";
    private static final String USER_ADDED_SUCCESSFULLY_TEMPLATE = "User (%s) was successfully added and is now being tracked.";
    private static final String USER_NOT_FOUND_TEMPLATE = "Could not start tracking user %s. That user could not be found" +
            " on the hiscores. Please gain some levels and try again!";

    private final UserService userService;
    private final UserUpdateService userUpdateService;
    private final OsrsHiscoreLiteClient osrsHiscoreLiteClient;

    @Inject
    public UserDisplayService(UserService userService, UserUpdateService userUpdateService, OsrsHiscoreLiteClient osrsHiscoreLiteClient) {
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
        this.osrsHiscoreLiteClient = Objects.requireNonNull(osrsHiscoreLiteClient, "OsrsHiscoreLiteClient is required.");
        this.userUpdateService = Objects.requireNonNull(userUpdateService, "UserUpdateService is required.");
    }

    public String addUser(String message) {
        logger.info("Adding user: {}", message);
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            return ADD_USER_USAGE;
        }

        String username = Arrays.stream(parts, 1, parts.length)
                .collect(Collectors.joining(" "));

        Optional<OsrsHiscoreLiteData> hiscoreData = osrsHiscoreLiteClient.getHiscoresForUser(username);

        if (hiscoreData.isEmpty()) {
            return String.format(USER_NOT_FOUND_TEMPLATE, username);
        }

        User user = UserBuilder.newBuilder().name(username).build();
        try {
            userService.createUser(user);
        } catch (ServiceException ex) {
            return String.format(USER_ALREADY_TRACKED_TEMPLATE, username);
        } catch (Exception ex) {
            return GENERIC_ERROR;
        }

        return String.format(USER_ADDED_SUCCESSFULLY_TEMPLATE, username);
    }

    public String updateUser(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            return UPDATE_USER_USAGE;
        }

        boolean afterUsername = false;
        Map<String, String> args = new HashMap<>();
        List<String> usernameParts = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            char currentChar = parts[i].charAt(0);
            if (currentChar == '-') {
                if (parts[i].length() < 2) {
                   return UPDATE_USER_USAGE;
                }

                if (parts.length - 1 == i) {
                    return UPDATE_USER_USAGE;
                }

                args.put(parts[i], parts[i++ + 1]);
                afterUsername = true;
            } else {
                if (afterUsername) {
                    return UPDATE_USER_USAGE;
                } else {
                    usernameParts.add(parts[i]);
                }
            }
        }

        if (args.size() < 1 || !(args.containsKey("-d") || args.containsKey("-h")) ) {
            return UPDATE_USER_USAGE;
        }


        Instant now = Instant.now();
        if (args.containsKey("-d"))  {
            String amount = args.get("-d");
            try {
                int amountNumber = Integer.parseInt(amount);
                now = now.minus(amountNumber, ChronoUnit.DAYS);
            } catch (Exception ex) {
                return UPDATE_USER_USAGE;
            }
        }

        if (args.containsKey("-h")) {
            String amount = args.get("-h");
            try {
                int amountNumber = Integer.parseInt(amount);
                now = now.minus(amountNumber, ChronoUnit.HOURS);
            } catch (Exception ex) {
                return UPDATE_USER_USAGE;
            }
        }

        String username = String.join(" ", usernameParts);
        try {
            User user = UserBuilder.newBuilder().name(username).build();
            Map<HiscoreReference, Hiscore> hiscores = userUpdateService.updateHiscoresFor(user, now);
            return makeHiscoreDisplayBean(user, hiscores).getMessage();
        } catch (Exception ex) {
            logger.error("An error occurred while updating user: ", ex);
            return GENERIC_ERROR;
        }
    }

    public HiscoreDisplayBean makeHiscoreDisplayBean(User user, Map<HiscoreReference, Hiscore> hiscores) {
        HiscoreDisplayBean bean = new HiscoreDisplayBean();
        bean.setUser(user.getName());

        Hiscore newScore = hiscores.get(HiscoreReference.NEW);
        Hiscore oldScore = hiscores.get(HiscoreReference.OLD);

        Duration timeDifference = Duration.between(oldScore.getUpdateTime(), newScore.getUpdateTime());
        int days = (int)timeDifference.toDaysPart();
        int hours = days * 24 + timeDifference.toHoursPart();
        bean.setHoursSinceLastUpdate(hours);
        bean.setMinutesSinceLastUpdate(timeDifference.toMinutesPart());

        for (Skill skill : Skill.values()) {
            int newXp = newScore.getXp(skill);
            int oldXp = oldScore.getXp(skill);

            int newLevels = newScore.getLevel(skill);
            int oldLevels = oldScore.getLevel(skill);

            int newRank = newScore.getRank(skill);
            int oldRank = oldScore.getRank(skill);

            if (newXp > oldXp) {
                String skillDisplayString = capitalizeFirstLetter(skill.name());
                bean.addXpGained(skillDisplayString, NumberFormat.getInstance().format(newXp - oldXp) + " xp");
                bean.addLevelsGained(skillDisplayString, newLevels - oldLevels + " L");
                if (oldRank - newRank != 0) {
                    String updatedRank = NumberFormat.getInstance().format(oldRank - newRank) + " R";
                    if (oldRank == -1) {
                        bean.addRanksGained(skillDisplayString, updatedRank + ", NEW");
                    } else {
                        bean.addRanksGained(skillDisplayString, updatedRank);
                    }
                } else {
                    bean.addRanksGained(skillDisplayString, "0 R");
                }
            }
        }

        return bean;
    }

    private String capitalizeFirstLetter(String string) {
       return string.substring(0, 1).toUpperCase() + string.toLowerCase().substring(1);
    }
}
