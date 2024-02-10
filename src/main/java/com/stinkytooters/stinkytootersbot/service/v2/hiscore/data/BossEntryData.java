package com.stinkytooters.stinkytootersbot.service.v2.hiscore.data;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.BossEntry;

public class BossEntryData {

    private long id;
    private long hiscoreEntryId;
    private long bossId;
    private long killcount;
    private long rank;

    public static BossEntryData from (Long hiscoreEntryId, BossEntry bossEntry) {
        BossEntryData bossEntryData = new BossEntryData();
        bossEntryData.setId(bossEntry.getId());
        bossEntryData.setBossId(bossEntry.getBoss().getBossId());
        bossEntryData.setHiscoreEntryId(hiscoreEntryId);
        bossEntryData.setKillcount(bossEntry.getKillcount());
        bossEntryData.setRank(bossEntry.getRank());
        return bossEntryData;
    }

    public BossEntry toBossEntry() {
        BossEntry bossEntry = new BossEntry();
        bossEntry.setId(id);
        bossEntry.setBoss(Boss.fromId(bossId));
        bossEntry.setKillcount(killcount);
        bossEntry.setRank(rank);
        return bossEntry;
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

    public long getBossId() {
        return bossId;
    }

    public void setBossId(long bossId) {
        this.bossId = bossId;
    }

    public long getKillcount() {
        return killcount;
    }

    public void setKillcount(long killcount) {
        this.killcount = killcount;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }
}
