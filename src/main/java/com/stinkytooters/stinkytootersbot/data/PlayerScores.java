package com.stinkytooters.stinkytootersbot.data;

import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreLiteData;

public class PlayerScores {

    private String playerName;
    private OsrsHiscoreLiteData scores;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public OsrsHiscoreLiteData getScores() {
        return scores;
    }

    public void setScores(OsrsHiscoreLiteData scores) {
        this.scores = scores;
    }
}
