package com.stinkytooters.stinkytootersbot.scripts;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.BossEntry;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.HiscoreV2Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Named
public class FixupZulrahData {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final HiscoreV2Service hiscoreV2Service;

    @Inject
    public FixupZulrahData(HiscoreV2Service hiscoreV2Service) {
        this.hiscoreV2Service = Objects.requireNonNull(hiscoreV2Service, "HiscoreV2Service is required.");
    }


    public void run() {
        List<HiscoreV2> allHiscores = hiscoreV2Service.getAllHiscores();
        logger.info("Retrieved {} hiscores.", allHiscores.size());

        Map<Long, List<HiscoreV2>> hiscoresByUser = allHiscores.stream()
                .collect(Collectors.groupingBy(HiscoreV2::getUserId));

        for (Map.Entry<Long, List<HiscoreV2>> entry : hiscoresByUser.entrySet()) {
            Long userId = entry.getKey();
            List<HiscoreV2> hiscoresForUser = entry.getValue();
            hiscoresForUser = hiscoresForUser.stream()
                    .sorted(Comparator.comparing(HiscoreV2::getUpdateTime))
                    .collect(Collectors.toList());

            long maxZurlahKc = -1;
            for (HiscoreV2 hiscoreV2 : hiscoresForUser) {
                BossEntry zulrahEntry = hiscoreV2.getBosses().get(Boss.ZULRAH);
                maxZurlahKc = Math.max(maxZurlahKc, zulrahEntry.getKillcount());
                if (maxZurlahKc > zulrahEntry.getKillcount()) {
                    logger.info("Updating hiscore {} to have zulrah kc {}.", hiscoreV2.getUpdateTime(), maxZurlahKc);
                    zulrahEntry.setKillcount(maxZurlahKc);
                    hiscoreV2Service.updateBossEntry(hiscoreV2.getId(), zulrahEntry);
                }
            }
        }
        logger.info("done");
    }

}
