package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.Skill;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;

public class Hiscore {

    private long id = -1;
    private long userId = -1;
    private Instant updateTime;

    // TODO: these should all be a type like 'SkillMap' and should be initialized by default.
    private final Map<Skill, Integer> xp;
    private final Map<Skill, Integer> rank;
    private final Map<Skill, Integer> level;

    public Hiscore() {
        xp = new TreeMap<>();
        rank = new TreeMap<>();
        level = new TreeMap<>();
    }

    public void addXp(Skill skill, int amount) {
        xp.put(skill, amount);
    }

    public void addRank(Skill skill, int amount) {
        rank.put(skill, amount);
    }

    public void addLevel(Skill skill, int amount) {
        level.put(skill, amount);
    }

    public void setXp(Map<Skill, Integer> xp) {
       this.xp.clear();
       xp.forEach(this::addXp);
    }

    public void setRank(Map<Skill, Integer> rank) {
        this.rank.clear();
        rank.forEach(this::addRank);
    }

    public void setLevel(Map<Skill, Integer> level) {
        this.level.clear();
        level.forEach(this::addLevel);
    }

    public Map<Skill, Integer> getXp() {
        return xp;
    }

    public Map<Skill, Integer> getRank() {
        return rank;
    }

    public Map<Skill, Integer> getLevel() {
        return level;
    }

    public int getXp(Skill skill) {
        return xp.getOrDefault(skill, -1);
    }

    public int getRank(Skill skill) {
        return rank.getOrDefault(skill, -1);
    }

    public int getLevel(Skill skill) {
        return level.getOrDefault(skill, -1);
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
        sb.append(", level=").append(level);
        sb.append('}');
        return sb.toString();
    }
}

