package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import java.util.stream.Stream;

public enum Skill {
    OVERALL(1L),
    ATTACK(2L),
    DEFENCE(3L),
    STRENGTH(4L),
    HITPOINTS(5L),
    RANGED(6L),
    PRAYER(7L),
    MAGIC(8L),
    COOKING(9L),
    WOODCUTTING(10L),
    FLETCHING(11L),
    FISHING(12L),
    FIREMAKING(13L),
    CRAFTING(14L),
    SMITHING(15L),
    MINING(16L),
    HERBLORE(17L),
    AGILITY(18L),
    THIEVING(19L),
    SLAYER(20L),
    FARMING(21L),
    RUNECRAFT(22L),
    HUNTER(23L),
    CONSTRUCTION(24L);

    public static Skill fromId(long id) {
        return Stream.of(values())
                .filter(s -> s.getSkillId() == id)
                .findFirst()
                .orElse(null);

    }

    private final long skillId;

    Skill(Long skillId) {
        this.skillId = skillId;
    }

    public long getSkillId() {
        return skillId;
    }
}
