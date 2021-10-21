package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import javax.inject.Named;
import java.io.InputStream;

@Named
public class OsrsHiscoreObjectMapper {

    public OsrsHiscoreLiteData deserialize(InputStream stream) throws Exception {
        try {
            String data = new String(stream.readAllBytes());
            String[] hiscores = data.split("\n");
            Skill[] skills = Skill.values();

            if (hiscores.length < skills.length) {
                // TODO: deserialization exception
            }

            OsrsHiscoreLiteData ret = new OsrsHiscoreLiteData();

            for (int i = 0; i < skills.length; i++) {
                String[] parts = hiscores[i].split(",");

                if (parts.length != 3) {
                    // TODO: deserialization exception
                }

                int rank = Integer.parseInt(parts[0]);
                int level = Integer.parseInt(parts[1]);
                int xp = Integer.parseInt(parts[2]);

                ret.setSkill(skills[i], new OsrsHiscoreLiteDataEntry(rank, level, xp));
            }

            return ret;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
