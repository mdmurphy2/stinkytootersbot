package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import java.util.Map;
import java.util.TreeMap;

public class OsrsHiscoreLiteData {

    private final Map<Skill, OsrsHiscoreLiteDataEntry> hiscores;

    public OsrsHiscoreLiteData() {
        this.hiscores = new TreeMap<>();
        for (Skill skill : Skill.values()) {
            hiscores.put(skill, new OsrsHiscoreLiteDataEntry());
        }
    };

    public void setSkill(Skill skill, OsrsHiscoreLiteDataEntry entry)  {
        hiscores.put(skill, entry);
    }

    public OsrsHiscoreLiteDataEntry getSkill(Skill skill) {
        return hiscores.get(skill);
    }

    public int getSkillXpOr(Skill skill, int value) {
        if (hiscores.containsKey(skill)) {
            return hiscores.get(skill).getXp();
        } else {
            return value;
        }
    }

    public int getRankOr(Skill skill, int value) {
        if (hiscores.containsKey(skill)) {
            return hiscores.get(skill).getRank();
        } else {
            return value;
        }
    }

    public int getLevelOr(Skill skill, int value) {
        if (hiscores.containsKey(skill)) {
            return hiscores.get(skill).getLevel();
        } else {
            return value;
        }
    }

    public Map<Skill, OsrsHiscoreLiteDataEntry> getHiscores() {
        return hiscores;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OsrsHiscoreLiteData{");
        sb.append("hiscores=").append(hiscores);
        sb.append('}');
        return sb.toString();
    }
}
