package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import java.util.Map;
import java.util.TreeMap;

public class OsrsHiscoreLiteData {

    private final Map<HiscoreEntry, OsrsHiscoreLiteDataEntry> hiscores;

    public OsrsHiscoreLiteData() {
        this.hiscores = new TreeMap<>();
        for (HiscoreEntry hiscoreEntry : HiscoreEntry.values()) {
            hiscores.put(hiscoreEntry, new OsrsHiscoreLiteDataEntry());
        }
    };

    public void setHiscoreEntry(HiscoreEntry hiscoreEntry, OsrsHiscoreLiteDataEntry entry)  {
        hiscores.put(hiscoreEntry, entry);
    }

    public OsrsHiscoreLiteDataEntry getEntry(HiscoreEntry hiscoreEntry) {
        return hiscores.get(hiscoreEntry);
    }

    public int getSkillOrScoreOr(HiscoreEntry hiscoreEntry, int value) {
        if (hiscores.containsKey(hiscoreEntry)) {
            return hiscores.get(hiscoreEntry).getXp();
        } else {
            return value;
        }
    }

    public int getRankOr(HiscoreEntry hiscoreEntry, int value) {
        if (hiscores.containsKey(hiscoreEntry)) {
            return hiscores.get(hiscoreEntry).getRank();
        } else {
            return value;
        }
    }

    public int getLevelOr(HiscoreEntry hiscoreEntry, int value) {
        if (hiscores.containsKey(hiscoreEntry)) {
            return hiscores.get(hiscoreEntry).getLevelOrScore();
        } else {
            return value;
        }
    }

    public Map<HiscoreEntry, OsrsHiscoreLiteDataEntry> getHiscores() {
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
