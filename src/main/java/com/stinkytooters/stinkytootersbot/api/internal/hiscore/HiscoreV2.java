package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class HiscoreV2 {

    private Long id = -1L;
    private Long userId = -1L;
    private Instant updateTime;

    private final Map<Skill, SkillEntry> skills = new HashMap<>();
    private final Map<Boss, BossEntry> bosses = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Map<Skill, SkillEntry> getSkills() {
        return skills;
    }

    public void addSkill(SkillEntry skillEntry) {
        if (skillEntry.getSkill() != null) {
            skills.put(skillEntry.getSkill(), skillEntry);
        }
    }

    public Map<Boss, BossEntry> getBosses() {
        return bosses;
    }

    public void addBoss(BossEntry bossEntry) {
        if (bossEntry.getBoss() != null) {
            bosses.put(bossEntry.getBoss(), bossEntry);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HiscoreV2.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("userId=" + userId)
                .add("updateTime=" + updateTime)
                .add("skills=" + skills)
                .add("bosses=" + bosses)
                .toString();
    }
}
