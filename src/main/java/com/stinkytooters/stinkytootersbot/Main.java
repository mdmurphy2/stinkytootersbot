package com.stinkytooters.stinkytootersbot;

import com.stinkytooters.stinkytootersbot.clients.StinkyTooterDiscordClient;
import com.stinkytooters.stinkytootersbot.jobs.SchedulingConfiguration;
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
    private StinkyTooterDiscordClient stinkyTooterDiscordClient;

    @Inject
    private SchedulingConfiguration schedulingConfiguration;


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
        stinkyTooterDiscordClient.start();
    }
}
