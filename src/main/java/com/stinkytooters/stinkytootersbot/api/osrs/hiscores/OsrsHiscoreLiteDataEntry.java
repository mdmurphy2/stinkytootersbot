package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

public class OsrsHiscoreLiteDataEntry {

    private final int rank;
    private final int level;
    private final int xp;

    public OsrsHiscoreLiteDataEntry(int rank, int level, int xp) {
        this.rank = rank;
        this.level = level;
        this.xp = xp;
    }

    public OsrsHiscoreLiteDataEntry() {
        rank = -1;
        level = -1;
        xp = -1;
    }

    public int getRank() {
        return rank;
    }

    public int getLevel() {
        return level;
    }

    public int getXp() {
        return xp;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OsrsHiscoreLiteDataEntry{");
        sb.append("rank=").append(rank);
        sb.append(", level=").append(level);
        sb.append(", xp=").append(xp);
        sb.append('}');
        return sb.toString();
    }
}
