package com.stinkytooters.stinkytootersbot.service.v2.hiscore.data;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.SkillEntry;

import java.util.StringJoiner;

public class SkillEntryData {

    private long id;
    private long hiscoreEntryId;
    private long skillId;
    private long xp;
    private long rank;
    private long level;

    public static SkillEntryData from(Long hiscoreEntryId, SkillEntry skillEntry) {
        SkillEntryData skillEntryData = new SkillEntryData();
        skillEntryData.setId(skillEntry.getId());
        skillEntryData.setHiscoreEntryId(hiscoreEntryId);
        skillEntryData.setSkillId(skillEntry.getSkill().getSkillId());
        skillEntryData.setXp(skillEntry.getXp());
        skillEntryData.setRank(skillEntry.getRank());
        skillEntryData.setLevel(skillEntry.getLevel());
        return skillEntryData;
    }

    public SkillEntry toSkillEntry() {
        SkillEntry skillEntry = new SkillEntry();
        skillEntry.setSkill(Skill.fromId(skillId));
        skillEntry.setLevel(level);
        skillEntry.setXp(xp);
        skillEntry.setRank(rank);
        skillEntry.setId(id);
        return skillEntry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHiscoreEntryId() {
        return hiscoreEntryId;
    }

    public void setHiscoreEntryId(long hiscoreEntryId) {
        this.hiscoreEntryId = hiscoreEntryId;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SkillEntryData.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("hiscoreEntryId=" + hiscoreEntryId)
                .add("skillId=" + skillId)
                .add("xp=" + xp)
                .add("rank=" + rank)
                .add("level=" + level)
                .toString();
    }
}
