package com.stinkytooters.stinkytootersbot.jobs.hiscores;

import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.stinkytooters.stinkytootersbot.service.user.UserUpdateService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Named
public class HydrateHiscoresJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UserService userService;
    private final UserUpdateService userUpdateService;

    @Inject
    public HydrateHiscoresJob(UserService userService, UserUpdateService userUpdateService) {
        this.userService = Objects.requireNonNull(userService, "UserService is required.");
        this.userUpdateService = Objects.requireNonNull(userUpdateService, "UserUpdateService is required.");
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Begin hydrating hiscores");
        hydrateUsers(userService.getAllUsers());
    }

    private void hydrateUsers(List<User> users) {
        for (User user : users) {
            try {
                if (user.getStatus() == UserStatus.ACTIVE) {
                    hydrate(user);
                }
            } catch (Exception ex) {
                logger.warn("Failed to hydrate user: {}", user);
            }
        }
    }

    private void hydrate(User user) {
        int retryAmount = 5;

        while(retryAmount > 0) {
            try {
                userUpdateService.updateHiscoresFor(user, Instant.now());
                return;
            } catch (Exception ex) {
                retryAmount--;
            }
        }

        logger.error("Failed to hydrate hiscores for user: {}", user);
    }

}
