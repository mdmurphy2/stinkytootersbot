package com.stinkytooters.stinkytootersbot;

import com.stinkytooters.stinkytootersbot.clients.OsrsHiscoreLiteClient;
import com.stinkytooters.stinkytootersbot.clients.StinkyTooterDiscordClient;
import com.stinkytooters.stinkytootersbot.data.PlayerScores;
import com.stinkytooters.stinkytootersbot.data.StinkyTootersDataStore;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.HiscoreDiff;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreLiteDataComparator;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.Skill;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.inject.Inject;
import javax.swing.text.NumberFormatter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@SpringBootApplication
@ComponentScan(basePackages = "com.stinkytooters")
public class Main implements CommandLineRunner {

    private final int COLUMN_WIDTH = 35;

    @Inject
    private ApplicationContext context;

    @Inject
    private OsrsHiscoreLiteClient osrsHiscoreLiteClient;

    @Inject
    private StinkyTooterDiscordClient stinkyTooterDiscordClient;

    @Inject
    private StinkyTootersDataStore stinkyTootersDataStore;


    public static void main(String[] args) throws IOException {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String player = "Merfee";

        PlayerScores oldScoresData = stinkyTootersDataStore.getHiscore(player);

        OsrsHiscoreLiteData oldScores = new OsrsHiscoreLiteData();
        if (oldScoresData != null) {
            oldScores = oldScoresData.getScores();
        }

        OsrsHiscoreLiteData newScores = osrsHiscoreLiteClient.getHiscoresForUser(player);
        stinkyTootersDataStore.updateHiscore(player, newScores);

        Map<Skill, HiscoreDiff> differenceMap = OsrsHiscoreLiteDataComparator.compare(oldScores, newScores);
        if (!differenceMap.isEmpty()) {
            String message = createUpdateMessage(player, differenceMap);
            stinkyTooterDiscordClient.sendMessage(message) ;
        } else {
            stinkyTooterDiscordClient.sendMessage(">>> :poop: **" + player + "**\n\n*Gained no xp, what a stinky tootin' loser.*");
        }
    }

    private String createUpdateMessage(String player, Map<Skill, HiscoreDiff> differenceMap) {

        StringBuilder message = new StringBuilder(1024);
        message.append("```\n" + player + "\n\n");

        int columnCount = 0;
        Queue<HiscoreDiff> scoresToPrint = new LinkedList<>();
        for (Map.Entry<Skill, HiscoreDiff> entry : differenceMap.entrySet()) {
            Skill skill = entry.getKey();
            HiscoreDiff diff = entry.getValue();
            scoresToPrint.add(diff);

            message.append(capitalizeFirstLetter(skill.name()));

            int paddingToAdd = COLUMN_WIDTH - skill.name().length();
            message.append(" ".repeat(Math.max(0, paddingToAdd)));
            columnCount++;

            if (columnCount == 3) {
                columnCount = 0;
                flushQueue(scoresToPrint, message);
                message.append("\n\n");
            }
        }
        flushQueue(scoresToPrint, message);
        message.append("```");

        return message.toString();
    }

    private String capitalizeFirstLetter(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1).toLowerCase();
    }

    private void flushQueue(Queue<HiscoreDiff> scoresToPrint, StringBuilder message) {
        message.append("\n");
        while (!scoresToPrint.isEmpty()) {
            HiscoreDiff currentDiff = scoresToPrint.remove();

            String levels = String.valueOf(currentDiff.getLevelsGained());
            String formattedXp = NumberFormat.getInstance().format(currentDiff.getXpGained());
            String formattedRanks = NumberFormat.getInstance().format(currentDiff.getRanksGained());

            String toAppend = formattedXp + " xp";
            if (currentDiff.getLevelsGained() > 0) {
                toAppend += " | " + levels + "L";
            }

            if (currentDiff.getRanksGained() > 0) {
                toAppend += " | " + formattedRanks + "R";
            }

            toAppend = toAppend.substring(0, Math.min(toAppend.length(), COLUMN_WIDTH));
            message.append(toAppend);

            if (!scoresToPrint.isEmpty()) {
                message.append(" ".repeat(Math.max(0, COLUMN_WIDTH - toAppend.length())));
            }
        }
    }

    }
