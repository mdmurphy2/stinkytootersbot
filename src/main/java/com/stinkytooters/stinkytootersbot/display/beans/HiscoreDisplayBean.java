package com.stinkytooters.stinkytootersbot.display.beans;

import java.util.*;

import static java.lang.Math.max;

public class HiscoreDisplayBean {

    private static final int COLUMN_WIDTH = 35;

    private String user;
    private int hoursSinceLastUpdate;
    private int minutesSinceLastUpdate;

    private final Map<String, String> xpGained;
    private final Map<String, String> levelsGained;
    private final Map<String, String> ranksGained;

    public HiscoreDisplayBean() {
        xpGained = new LinkedHashMap<>();
        levelsGained = new LinkedHashMap<>();
        ranksGained = new LinkedHashMap<>();
    }

    public void addXpGained(String skill, String xp) {
        xpGained.put(skill, xp);
    }

    public void addLevelsGained(String skill, String levels) {
        levelsGained.put(skill, levels);
    }

    public void addRanksGained(String skill, String ranks) {
        ranksGained.put(skill, ranks);
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setHoursSinceLastUpdate(int hours) {
        this.hoursSinceLastUpdate = hours;
    }

    public void setMinutesSinceLastUpdate(int minutes) {
        this.minutesSinceLastUpdate = minutes;
    }

    public String getMessage() {
        StringBuilder message  = new StringBuilder(1024);
        message.append("```");

        message.append(String.format("%s - (Gains for past: %d hour(s) %d minute(s))", user, hoursSinceLastUpdate, minutesSinceLastUpdate));
        message.append("\n\n");

        int index = 0;
        Queue<List<String>> toPrint = new LinkedList<>();
        for (Map.Entry<String, String> entry : xpGained.entrySet()) {
            String skill = entry.getKey();

            String xpGained = entry.getValue();
            String levelsGained = this.levelsGained.get(skill);
            String ranksGained = this.ranksGained.get(skill);
            toPrint.add(Arrays.asList(xpGained, levelsGained, ranksGained));

            if ((index + 1) % 3 == 0) {
                // printing xp differences
                message.append(skill);
                message.append(" ".repeat(max(COLUMN_WIDTH - skill.length(), 0)));
                message.append("\n");

                while(!toPrint.isEmpty()) {
                    List<String> gainsList = toPrint.remove();
                    xpGained = gainsList.get(0);
                    levelsGained = gainsList.get(1);
                    ranksGained = gainsList.get(2);

                    String gains = xpGained + " | " + levelsGained + " | " + ranksGained;
                    message.append(gains);
                    message.append(" ".repeat(max(COLUMN_WIDTH - gains.length(), 0)));
                }
                message.append("\n\n");
            } else {
                // printing column names
                message.append(skill);
                message.append(" ".repeat(max(COLUMN_WIDTH - skill.length(), 0)));
            }

            index++;
        }

        // printing xp differences
        message.append("\n");
        while(!toPrint.isEmpty()) {
            List<String> gainsList = toPrint.remove();
            String xpGained = gainsList.get(0);
            String levelsGained = gainsList.get(1);
            String ranksGained = gainsList.get(2);

            String gains = xpGained + " | " + levelsGained + " | " + ranksGained;
            message.append(gains);
            message.append(" ".repeat(COLUMN_WIDTH - gains.length()));
        }
        message.append("\n```");

        return message.toString();
    }
}
