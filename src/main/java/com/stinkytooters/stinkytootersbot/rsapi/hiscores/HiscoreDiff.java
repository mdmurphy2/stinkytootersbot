package com.stinkytooters.stinkytootersbot.rsapi.hiscores;

public class HiscoreDiff {

    private final int ranksGained;
    private final int levelsGained;
    private final int xpGained;

    public HiscoreDiff(int ranksGained, int levelsGained, int xpGained) {
        this.ranksGained = ranksGained;
        this.levelsGained = levelsGained;
        this.xpGained = xpGained;
    }

    public int getRanksGained() {
        return ranksGained;
    }

    public int getLevelsGained() {
        return levelsGained;
    }

    public int getXpGained() {
        return xpGained;
    }
}
