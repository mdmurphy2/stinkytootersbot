package com.stinkytooters.stinkytootersbot.service.v2.hiscore.data;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.StringJoiner;

public class HiscoreEntryData {

    private Long id = -1L;
    private Long userId = -1L;
    private Timestamp timestamp;

    public static HiscoreEntryData from(HiscoreV2 hiscoreV2) {
        HiscoreEntryData entryData = new HiscoreEntryData();
        entryData.setUserId(hiscoreV2.getUserId());
        if (hiscoreV2.getUpdateTime() != null) {
            entryData.setTimestamp(Timestamp.from(hiscoreV2.getUpdateTime()));
        } else {
            entryData.setTimestamp(Timestamp.from(Instant.now()));
        }
        return entryData;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HiscoreEntryData.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("userId=" + userId)
                .add("timestamp=" + timestamp)
                .toString();
    }
}
