package com.stinkytooters.stinkytootersbot.jobs.hiscores;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;
import com.stinkytooters.stinkytootersbot.bot.DiscordBot;
import com.stinkytooters.stinkytootersbot.display.beans.HiscoreDisplayBean;
import com.stinkytooters.stinkytootersbot.display.user.UserDisplayService;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService;
import net.dv8tion.jda.api.JDA;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Named
public class EmitDaysHiscoresToDiscordJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final UserUpdateService userUpdateService;
    private final UserDisplayService userDisplayService;
    private final JDA discordClient;
    private final String channelId;

    @Inject
    public EmitDaysHiscoresToDiscordJob(
            UserService userService,
            UserUpdateService userUpdateService,
            UserDisplayService userDisplayService,
            JDA discordClient,
            @Value("${job.emit.hiscores.channel.id}") String channelId
    ) {
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
        this.userUpdateService = Objects.requireNonNull(userUpdateService, "UserUpdateServce is required.");
        this.userDisplayService = Objects.requireNonNull(userDisplayService, "UserDisplayService is required.");
        this.discordClient = Objects.requireNonNull(discordClient, "DiscordClient is required.");
        this.channelId = Objects.requireNonNull(channelId, "job.emit.hiscores.channel.id is required.");
    }


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        run();
    }

    public void run() {
        Instant oneDayAgo = Instant.now().minus(24, ChronoUnit.HOURS);
        List<User> users = userService.getAllUsers();

        List<User> usersCopy = new ArrayList<>();
        for (User user : users) {
            if (user.getName().equalsIgnoreCase("just yanks")) {
                usersCopy.add(0, user);
            } else {
                usersCopy.add(user);
            }
        }

        for (User user : usersCopy) {
            if (user.getStatus() == UserStatus.ACTIVE) {
                try {
                    Map<UserUpdateService.HiscoreReference, Hiscore> oldNew = userUpdateService.updateHiscoresFor(user, oneDayAgo);
                    HiscoreDisplayBean displayBean = userDisplayService.makeHiscoreDisplayBean(user, oldNew);
                    discordClient.getTextChannelById(channelId).sendMessage(displayBean.getMessage()).complete();
                } catch (Exception ex) {
                    logger.error("Failed to emit hiscores for user ({})", user, ex);
                }
            }
        }
    }
}
