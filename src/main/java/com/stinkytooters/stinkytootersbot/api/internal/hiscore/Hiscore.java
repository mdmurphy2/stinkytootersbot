package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class Hiscore {

    private long id = -1;
    private long userId = -1;
    private Instant updateTime;

    // TODO: these should all be a type like 'SkillMap' and should be initialized by default.
    private final Map<HiscoreEntry, Integer> xp;
    private final Map<HiscoreEntry, Integer> rank;
    private final Map<HiscoreEntry, Integer> levelOrScore;

    public Hiscore() {
        xp = new TreeMap<>();
        rank = new TreeMap<>();
        levelOrScore = new TreeMap<>();
    }

    public void addXp(HiscoreEntry hiscoreEntry, int amount) {
        xp.put(hiscoreEntry, amount);
    }

    public void addRank(HiscoreEntry hiscoreEntry, int amount) {
        rank.put(hiscoreEntry, amount);
    }

    public void addLevelOrScore(HiscoreEntry hiscoreEntry, int amount) {
        levelOrScore.put(hiscoreEntry, amount);
    }

    public void setXp(Map<HiscoreEntry, Integer> xp) {
       this.xp.clear();
       xp.forEach(this::addXp);
    }

    public void setRank(Map<HiscoreEntry, Integer> rank) {
        this.rank.clear();
        rank.forEach(this::addRank);
    }

    public void setLevelOrScore(Map<HiscoreEntry, Integer> levelOrScore) {
        this.levelOrScore.clear();
        levelOrScore.forEach(this::addLevelOrScore);
    }

    public Map<HiscoreEntry, Integer> getXp() {
        return xp;
    }

    public Map<HiscoreEntry, Integer> getRank() {
        return rank;
    }

    public Map<HiscoreEntry, Integer> getLevelOrScore() {
        return levelOrScore;
    }

    public int getXp(HiscoreEntry hiscoreEntry) {
        return xp.getOrDefault(hiscoreEntry, -1);
    }

    public int getRank(HiscoreEntry hiscoreEntry) {
        return rank.getOrDefault(hiscoreEntry, -1);
    }

    public int getLevelOrScore(HiscoreEntry hiscoreEntry) {
        return levelOrScore.getOrDefault(hiscoreEntry, -1);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Hiscore{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", xp=").append(xp);
        sb.append(", rank=").append(rank);
        sb.append(", level=").append(levelOrScore);
        sb.append('}');
        return sb.toString();
    }
}

