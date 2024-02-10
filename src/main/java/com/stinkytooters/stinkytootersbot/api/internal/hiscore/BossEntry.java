package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import java.util.StringJoiner;

public class BossEntry {

    private Long id = -1L;
    private Boss boss;
    private Long killcount = -1L;
    private Long rank = -1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boss getBoss() {
        return boss;
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }

    public Long getKillcount() {
        return killcount;
    }

    public void setKillcount(Long killcount) {
        this.killcount = killcount;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", BossEntry.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("boss=" + boss)
                .add("killcount=" + killcount)
                .add("rank=" + rank)
                .toString();
    }
}
