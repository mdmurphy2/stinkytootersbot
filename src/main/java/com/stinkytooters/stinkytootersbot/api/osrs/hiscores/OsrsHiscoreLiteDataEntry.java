package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

public class OsrsHiscoreLiteDataEntry {

    private final int rank;
    private final int levelOrScore;
    private final int xp;

    public OsrsHiscoreLiteDataEntry(int rank, int levelOrScore, int xp) {
        this.rank = rank;
        this.levelOrScore = levelOrScore;
        this.xp = xp;
    }

    public OsrsHiscoreLiteDataEntry(int rank, int levelOrScore) {
        this(rank, levelOrScore, -1);
    }

    public OsrsHiscoreLiteDataEntry() {
        this(-1, -1, -1);
    }

    public int getRank() {
        return rank;
    }

    public int getLevelOrScore() {
        return levelOrScore;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OsrsHiscoreLiteDataEntry{");
        sb.append("rank=").append(rank);
        sb.append(", level=").append(levelOrScore);
        sb.append(", xp=").append(xp);
        sb.append('}');
        return sb.toString();
    }
}
