package com.stinkytooters.stinkytootersbot.service;

import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.Skill;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
import com.stinkytooters.stinkytootersbot.service.hiscore.data.HiscoreDao;
import com.stinkytooters.stinkytootersbot.service.hiscore.data.HiscoreData;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HiscoreServiceTest {

    private static final int ST_DIFFUSOR_ID = 1;
    private static final int MAX_XP = 200_000_000;
    private static final int MAX_RANK = 5_000_000;
    private static final int MAX_LEVEL = 99;

    @Mock
    private HiscoreDao hiscoreDao;

    @InjectMocks
    private HiscoreService hiscoreService;

    @Before
    public void setup() {
    }

    @Test
    public void getLatestHiscore_whenFound_expectHiscore() {
        // given: hiscore data that exists
        when(hiscoreDao.getLatestHiscoresByUserId(anyLong())).thenReturn(getHiscoreData());

        // when: getting the data
        Hiscore hiscore = hiscoreService.getLatestHiscore(ST_DIFFUSOR_ID);

        // expect: the data matches expectations
        assertNotNull(hiscore);
        assertEquals("Has levels for all skills", hiscore.getLevel().size(), Skill.values().length);
        assertEquals("Has rank for all skills", hiscore.getRank().size(), Skill.values().length);
        assertEquals("Has xp for all skills", hiscore.getXp().size(), Skill.values().length);
        verify(hiscoreDao, times(1)).getLatestHiscoresByUserId(anyLong());
    }

    @Test
    public void getLatestHiscore_whenNotFound_expectError() {
        // given: hiscore doesn't exist
        when(hiscoreDao.getLatestHiscoresByUserId(anyLong())).thenReturn(Optional.empty());

        // when getting the data, expect: error
        assertThrows(
                "Throws service exception when data is not found",
                ServiceException.class,
                () -> hiscoreService.getLatestHiscore(ST_DIFFUSOR_ID)
        );
    }

    private Optional<HiscoreData> getHiscoreData() {
        Hiscore hiscore = new Hiscore();
        hiscore.setId(1);
        hiscore.setUpdateTime(Instant.now());

        Random random = new Random();
        for (Skill skill : Skill.values()) {
            int xp = random.nextInt(MAX_XP) + 1;
            int rank = random.nextInt(MAX_RANK) + 1;
            int level = random.nextInt(MAX_LEVEL) + 1;

            hiscore.addXp(skill, xp);
            hiscore.addRank(skill, rank);
            hiscore.addLevel(skill, level);
        }

        return Optional.of(HiscoreData.from(hiscore));
    }



}
