package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import java.util.StringJoiner;

public class SkillEntry {

    private Long id = -1l;
    private Skill skill;
    private Long rank = -1L;
    private Long xp = -1L;
    private Long level = -1L;

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Long getXp() {
        return xp;
    }

    public void setXp(Long xp) {
        this.xp = xp;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkillEntry.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("skill=" + skill)
                .add("rank=" + rank)
                .add("xp=" + xp)
                .add("level=" + level)
                .toString();
    }
}
