package com.stinkytooters.stinkytootersbot.rsapi.hiscores;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;

@Named
public class OsrsHiscoreObjectMapper {

    public OsrsHiscoreLiteData deserialize(InputStream stream) throws IOException {
        String data = new String(stream.readAllBytes());
        String[] hiscores = data.split("\n");

        OsrsHiscoreLiteData ret = new OsrsHiscoreLiteData();

        Skill[] skills = Skill.values();

        for (int i = 0; i < skills.length; i++) {
            String[] parts = hiscores[i].split(",");
            assert(parts.length == 3);

            int rank = Integer.parseInt(parts[0]);
            int level = Integer.parseInt(parts[1]);
            int xp = Integer.parseInt(parts[2]);

            ret.setSkill(skills[i], new OsrsHiscoreLiteDataEntry(rank, level, xp));
        }

        return ret;
    }
}
