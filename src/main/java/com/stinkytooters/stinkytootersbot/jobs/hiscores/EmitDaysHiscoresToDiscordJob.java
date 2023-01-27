package com.stinkytooters.stinkytootersbot.jobs.hiscores;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;
import com.stinkytooters.stinkytootersbot.clients.StinkyTooterDiscordClient;
import com.stinkytooters.stinkytootersbot.display.beans.HiscoreDisplayBean;
import com.stinkytooters.stinkytootersbot.display.user.UserDisplayService;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService;
import discord4j.core.DiscordClient;
import discord4j.discordjson.json.gateway.UserUpdate;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Named
public class EmitDaysHiscoresToDiscordJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HiscoreService hiscoreService;
    private final UserService userService;
    private final UserUpdateService userUpdateService;
    private final UserDisplayService userDisplayService;
    private final StinkyTooterDiscordClient stinkyTootersDiscordClient;
    private final String channelId;

    public EmitDaysHiscoresToDiscordJob(
            HiscoreService hiscoreService,
            UserService userService,
            UserUpdateService userUpdateService,
            UserDisplayService userDisplayService,
            StinkyTooterDiscordClient stinkyTootersDiscordClient,
            @Value("${job.emit.hiscores.channel.id}") String channelId
    ) {
        this.hiscoreService = Objects.requireNonNull(hiscoreService, "HiscoreService is required.");
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
        this.userUpdateService = Objects.requireNonNull(userUpdateService, "UserUpdateServce is required.");
        this.userDisplayService = Objects.requireNonNull(userDisplayService, "UserDisplayService is required.");
        this.stinkyTootersDiscordClient = Objects.requireNonNull(stinkyTootersDiscordClient, "StinkyTootersDiscordClient is required.");
        this.channelId = Objects.requireNonNull(channelId, "job.emit.hiscores.channel.id is required.");
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Instant oneDayAgo = Instant.now().minus(24, ChronoUnit.HOURS);
        List<User> users = userService.getAllUsers();

        for (User user : users) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                try {
                    Map<UserUpdateService.HiscoreReference, Hiscore> oldNew = userUpdateService.updateHiscoresFor(user, oneDayAgo);
                    HiscoreDisplayBean displayBean = userDisplayService.makeHiscoreDisplayBean(user, oldNew);
                    stinkyTootersDiscordClient.sendMessage(channelId, displayBean.getMessage());
                } catch (Exception ex) {
                    logger.error("Failed to emit hiscores for user ({})", user, ex);
                }
            }
        }

    }
}
