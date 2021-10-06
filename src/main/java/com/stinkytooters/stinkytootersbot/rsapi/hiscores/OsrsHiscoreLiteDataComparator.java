package com.stinkytooters.stinkytootersbot.rsapi.hiscores;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class OsrsHiscoreLiteDataComparator {

    public static Map<Skill, HiscoreDiff> compare(OsrsHiscoreLiteData oldData, OsrsHiscoreLiteData newData) {

        Map<Skill, HiscoreDiff> hiscoreDifference = new TreeMap<>();

        for (Skill skill : Skill.values()) {
            int oldXp = oldData.getSkillXpOr(skill, 0);
            int newXp = newData.getSkillXpOr(skill, 0);

            int oldLevel = oldData.getLevelOr(skill, 0);
            int newLevel = newData.getLevelOr(skill, 0);

            int oldRank = oldData.getRankOr(skill, 0);
            int newRank = newData.getRankOr(skill, 0);

            if (newXp > oldXp) {
                hiscoreDifference.put(skill, new HiscoreDiff(oldRank - newRank, newLevel - oldLevel, newXp - oldXp));
            }
        }

        return hiscoreDifference;
    }

}
