package com.stinkytooters.stinkytootersbot;

import com.stinkytooters.stinkytootersbot.bot.DiscordBot;
import com.stinkytooters.stinkytootersbot.jobs.SchedulingConfiguration;
import com.stinkytooters.stinkytootersbot.jobs.hiscores.EmitDaysHiscoresToDiscordJob;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Inject;
import java.io.IOException;

@SpringBootApplication
@ComponentScan(basePackages = "com.stinkytooters")
public class Main implements CommandLineRunner {

    @Inject
    private DiscordBot discordBot;

    @Inject
    private SchedulingConfiguration schedulingConfiguration;

    @Inject
    private EmitDaysHiscoresToDiscordJob emitDaysHiscoresToDiscordJob;


    public static void main(String[] args) throws IOException {
        try {
            SpringApplication.run(Main.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(String... args) throws Exception {
        schedulingConfiguration.schedule();
        discordBot.listen();
        emitDaysHiscoresToDiscordJob.run();
    }
}
