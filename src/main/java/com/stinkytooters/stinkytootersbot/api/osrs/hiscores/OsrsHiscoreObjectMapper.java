package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import javax.inject.Named;
import java.io.InputStream;
import java.util.List;

@Named
public class OsrsHiscoreObjectMapper {

    public OsrsHiscoreLiteData deserialize(InputStream stream) throws Exception {
        try {
            String data = new String(stream.readAllBytes());
            String[] hiscores = data.split("\n");

            List<HiscoreEntry> hiscoreEntries = HiscoreEntry.getOrderedEntries();
            if (hiscores.length < hiscoreEntries.size()) {
                // TODO: deserialization exception
            }

            OsrsHiscoreLiteData hiscoreLiteData = new OsrsHiscoreLiteData();

            for (int i = 0; i < hiscoreEntries.size(); i++) {
                HiscoreEntry entry = hiscoreEntries.get(i);
                String[] parts = hiscores[i].split(",");

                if (parts.length != entry.getEntries()) {
                    // TODO: deserialization exception
                }

                if (entry.getEntries() == 3) {
                    int rank = Integer.parseInt(parts[0]);
                    int level = Integer.parseInt(parts[1]);
                    int xp = Integer.parseInt(parts[2]);
                    hiscoreLiteData.setHiscoreEntry(entry, new OsrsHiscoreLiteDataEntry(rank, level, xp));
                } else if (entry.getEntries() == 2) {
                    int rank = Integer.parseInt(parts[0]);
                    int score = Integer.parseInt(parts[1]);
                    hiscoreLiteData.setHiscoreEntry(entry, new OsrsHiscoreLiteDataEntry(rank, score));
                }
            }
            return hiscoreLiteData;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
