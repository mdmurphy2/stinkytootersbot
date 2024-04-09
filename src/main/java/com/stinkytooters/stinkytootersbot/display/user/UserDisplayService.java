package com.stinkytooters.stinkytootersbot.display.user;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.BossEntry;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.SkillEntry;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserBuilder;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.EHPData;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.clients.OsrsHiscoreLiteClient;
import com.stinkytooters.stinkytootersbot.display.beans.HiscoreDisplayBean;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService.HiscoreReference;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
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

    private static final String REMOVE_USER_USAGE = "Invalid syntax. Usage: !userremove <runescape username>";
    private static final String USER_REMOVED_SUCCESSFULLY_TEMPLATE = "User (%s) was successfully removed and is no longer being tracked.";

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

    public MessageCreateData addUser(String message) {
        logger.info("Adding user: {}", message);
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            return createMessage(ADD_USER_USAGE);
        }

        String username = Arrays.stream(parts, 1, parts.length)
                .collect(Collectors.joining(" "));

        Optional<OsrsHiscoreLiteData> hiscoreData = osrsHiscoreLiteClient.getHiscoresForUser(username);

        if (hiscoreData.isEmpty()) {
            return createMessage(String.format(USER_NOT_FOUND_TEMPLATE, username));
        }

        User user = UserBuilder.newBuilder().name(username).status(UserStatus.ACTIVE).build();
        try {
            if (userExists(user)) {
                userService.saveUser(user);
            } else {
                userService.createUser(user);
            }
        } catch (ServiceException ex) {
            return createMessage(String.format(USER_ALREADY_TRACKED_TEMPLATE, username));
        } catch (Exception ex) {
            return createMessage(GENERIC_ERROR);
        }

        return createMessage(String.format(USER_ADDED_SUCCESSFULLY_TEMPLATE, username));
    }

    public MessageCreateData inactivateUser(String message) {
        logger.info("Inactivating user: {}", message);
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            return createMessage(REMOVE_USER_USAGE);
        }

        String username = Arrays.stream(parts, 1, parts.length)
                .collect(Collectors.joining(" "));

        User user = UserBuilder.newBuilder().name(username).status(UserStatus.INACTIVE).build();
        try {
            userService.saveUser(user);
        } catch (Exception ex) {
            return createMessage(GENERIC_ERROR);
        }

        return createMessage(String.format(USER_REMOVED_SUCCESSFULLY_TEMPLATE, username));

    }

    public List<MessageCreateData> updateUser(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 2) {
            return Arrays.asList(createMessage(UPDATE_USER_USAGE));
        }

        boolean afterUsername = false;
        Map<String, String> args = new HashMap<>();
        List<String> usernameParts = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            char currentChar = parts[i].charAt(0);
            if (currentChar == '-') {
                if (parts[i].length() < 2) {
                   return Arrays.asList(createMessage(UPDATE_USER_USAGE));
                }

                if (parts.length - 1 == i) {
                    return Arrays.asList(createMessage(UPDATE_USER_USAGE));
                }

                args.put(parts[i], parts[i++ + 1]);
                afterUsername = true;
            } else {
                if (afterUsername) {
                    return Arrays.asList(createMessage(UPDATE_USER_USAGE));
                } else {
                    usernameParts.add(parts[i]);
                }
            }
        }

        if (args.size() < 1 || !(args.containsKey("-d") || args.containsKey("-h")) ) {
            return Arrays.asList(createMessage(UPDATE_USER_USAGE));
        }


        Instant now = Instant.now();
        if (args.containsKey("-d"))  {
            String amount = args.get("-d");
            try {
                int amountNumber = Integer.parseInt(amount);
                now = now.minus(amountNumber, ChronoUnit.DAYS);
            } catch (Exception ex) {
                return Arrays.asList(createMessage(UPDATE_USER_USAGE));
            }
        }

        if (args.containsKey("-h")) {
            String amount = args.get("-h");
            try {
                int amountNumber = Integer.parseInt(amount);
                now = now.minus(amountNumber, ChronoUnit.HOURS);
            } catch (Exception ex) {
                return Arrays.asList(createMessage(UPDATE_USER_USAGE));
            }
        }

        String username = String.join(" ", usernameParts);
        try {
            User user = UserBuilder.newBuilder().name(username).build();
            Map<HiscoreReference, HiscoreV2> hiscores = userUpdateService.updateHiscoresFor(user, now);

            HiscoreDisplayBean hiscoreDisplayBean = makeHiscoreDisplayBean(user, hiscores);
            return Arrays.asList(
                    createMessage(hiscoreDisplayBean.getSkillsMessage()),
                    createMessage(hiscoreDisplayBean.getBossesMessage())
            );
        } catch (Exception ex) {
            logger.error("An error occurred while updating user: {}", username, ex);
            return Arrays.asList(createMessage(GENERIC_ERROR));
        }
    }

    public HiscoreDisplayBean makeHiscoreDisplayBean(User user, Map<HiscoreReference, HiscoreV2> hiscores) {
        HiscoreDisplayBean bean = new HiscoreDisplayBean();
        bean.setUser(user.getName());

        HiscoreV2 newScore = hiscores.get(HiscoreReference.NEW);
        HiscoreV2 oldScore = hiscores.get(HiscoreReference.OLD);

        if (oldScore.getUpdateTime() != null) {
            Duration timeDifference = Duration.between(oldScore.getUpdateTime(), newScore.getUpdateTime());
            int days = (int)timeDifference.toDaysPart();
            int hours = days * 24 + timeDifference.toHoursPart();
            bean.setHoursSinceLastUpdate(hours);
            bean.setMinutesSinceLastUpdate(timeDifference.toMinutesPart());
        } else {
            bean.setHoursSinceLastUpdate(99999);
            bean.setMinutesSinceLastUpdate(99999);
        }

        EHPData ehpData = new EHPData();
        double totalEHP = 0;

        for (Skill skill : Skill.values()) {
            SkillEntry newSkillEntry = newScore.getSkills().get(skill);
            SkillEntry oldSkillEntry = oldScore.getSkills().get(skill);

            String hiscoreEntryDisplayString = getHiscoreEntryDisplayString(skill.name());
            if (oldSkillEntry != null) {
                if (newSkillEntry.getXp() > oldSkillEntry.getXp()) {
                    bean.addXpGained(hiscoreEntryDisplayString, NumberFormat.getInstance().format(newSkillEntry.getXp() - oldSkillEntry.getXp()) + " xp");
                    bean.addLevelsGained(hiscoreEntryDisplayString, newSkillEntry.getLevel() - oldSkillEntry.getLevel() + " L");

                    //Calculate EHP
                    double ehp = (newSkillEntry.getXp() - oldSkillEntry.getXp()) / ehpData.GetEHP(skill);

                    if(skill.equals(Skill.OVERALL)) {
                        //Do nothing
                    }
                    else if(ehp < 0) { //Magic is "0" time so return negative. Overall is updated below for "total ehp"
                        bean.addEHPGained(hiscoreEntryDisplayString, "0 EHP");
                    } else {
                        totalEHP += ehp;
                        bean.addEHPGained(hiscoreEntryDisplayString, String.format("%.2f", ehp) + " EHP");
                    }
                   
                }
                /* 
                Disable Ranks for now, I think EHP is more practical metric BUT leaving it incase we want to add EHP. I just think the message would be too long
                if (newSkillEntry.getRank() > oldSkillEntry.getRank()) {
                    String updatedRank = NumberFormat.getInstance().format(newSkillEntry.getRank() - oldSkillEntry.getRank()) + " R";
                    if (oldSkillEntry.getRank() == -1) {
                        bean.addRanksGained(hiscoreEntryDisplayString, "0 R, NEW");
                    } else {
                        bean.addRanksGained(hiscoreEntryDisplayString, updatedRank);
                    }
                } else {
                    bean.addRanksGained(hiscoreEntryDisplayString, "0 R");
                }
                */
            } else {
                if (newSkillEntry.getXp() > 0) {
                    bean.addXpGained(hiscoreEntryDisplayString, NumberFormat.getInstance().format(newSkillEntry.getXp()) + " xp");
                    //bean.addRanksGained(hiscoreEntryDisplayString, NumberFormat.getInstance().format(newSkillEntry.getRank()) + " R");
                    bean.addLevelsGained(hiscoreEntryDisplayString, newSkillEntry.getLevel() + " L");
                }
            }
        }

        for (Boss boss : Boss.values()) {
            BossEntry newBossEntry = newScore.getBosses().get(boss);
            BossEntry oldBossEntry = oldScore.getBosses().get(boss);
            String hiscoreEntryDisplayString = getHiscoreEntryDisplayString(boss.name());
            if (oldBossEntry != null) {
                if (newBossEntry.getKillcount() > oldBossEntry.getKillcount()) {
                    long oldBossScore = Math.max(oldBossEntry.getKillcount(), 0);
                    bean.addBossScoreGained(hiscoreEntryDisplayString, NumberFormat.getInstance().format(newBossEntry.getKillcount() - oldBossScore) + " kills");
                }
                if (newBossEntry.getRank() > oldBossEntry.getRank()) {
                    String updatedRank = NumberFormat.getInstance().format(newBossEntry.getRank() - oldBossEntry.getRank()) + " R";
                    if (oldBossEntry.getRank() == -1) {
                        bean.addRanksGained(hiscoreEntryDisplayString, "0 R, NEW");
                    } else {
                        bean.addRanksGained(hiscoreEntryDisplayString, updatedRank);
                    }
                } else {
                    bean.addRanksGained(hiscoreEntryDisplayString, "0 R");
                }
            } else {
                if (newBossEntry.getKillcount() > 0) {
                    bean.addBossScoreGained(hiscoreEntryDisplayString, NumberFormat.getInstance().format(newBossEntry.getKillcount()) + " kills");
                    bean.addRanksGained(hiscoreEntryDisplayString, NumberFormat.getInstance().format(newBossEntry.getRank()) + " R");
                }
            }
        }

        bean.addTotalEHPGained(String.format("%.2f", totalEHP) + " EHP");

        return bean;
    }

    private String getHiscoreEntryDisplayString(String string) {
        String result = "";
        if (string.contains("_")) {
            String[] parts = string.split("_");
            boolean first = true;
            for (String part : parts){
                if (first) {
                    result += capitalizeFirstLetter(part);
                    first = false;
                } else {
                    result += " " + capitalizeFirstLetter(part);
                }
            }
        } else {
            result = capitalizeFirstLetter(string);
        }
        return result;
    }

    private String capitalizeFirstLetter(String string) {
       return string.substring(0, 1).toUpperCase() + string.toLowerCase().substring(1);
    }

    private boolean userExists(User user) {
        try {
            userService.getUser(user);
            return true;
        } catch (ServiceException ex) {
            return false;
        }
    }

    private MessageCreateData createMessage(String message) {
        return new MessageCreateBuilder()
                .setContent(message)
                .build();
    }
}
