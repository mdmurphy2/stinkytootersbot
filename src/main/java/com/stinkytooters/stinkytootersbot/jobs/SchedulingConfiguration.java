package com.stinkytooters.stinkytootersbot.jobs;

import com.stinkytooters.stinkytootersbot.jobs.hiscores.EmitDaysHiscoresToDiscordJob;
import com.stinkytooters.stinkytootersbot.jobs.hiscores.HydrateHiscoresJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.Objects;

@Named
public class SchedulingConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final Scheduler stinkyTootersScheduler;

    @Inject
    public SchedulingConfiguration(Scheduler stinkyTootersScheduler) {
        this.stinkyTootersScheduler = Objects.requireNonNull(stinkyTootersScheduler, "QuartzScheduler is required.");
    }

    public void schedule() {
        try {
            stinkyTootersScheduler.scheduleJob(
                    JobBuilder.newJob(HydrateHiscoresJob.class)
                            .withIdentity("HydrateJob", "stinkytooters")
                            .build(),
                    TriggerBuilder.newTrigger()
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * * * ?"))
                            .build()
            );
            stinkyTootersScheduler.scheduleJob(
                    JobBuilder.newJob(EmitDaysHiscoresToDiscordJob.class)
                         .withIdentity("EmitDaysHiscoresToDiscordJob", "stinkytooters")
                        .build(),
                    TriggerBuilder.newTrigger()
                            .withSchedule(CronScheduleBuilder.cronSchedule("0 0 6 * * ?"))
                            .build()
            );
            stinkyTootersScheduler.start();
        } catch (SchedulerException ex) {
            logger.error("Could not schedule job.", ex);
        }
    }

}
