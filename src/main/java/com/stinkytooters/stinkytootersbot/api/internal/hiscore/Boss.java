package com.stinkytooters.stinkytootersbot.api.internal.hiscore;

import java.util.stream.Stream;

public enum Boss {
    GOTR(1L),
    ABYSSAL_SIRE(2L),
    ALCHEMICAL_HYDRA(3L),
    ARTIO(4L),
    BARROWS_CHESTS(5L),
    BRYOPHYTA(6L),
    CALLISTO(7L),
    COLOSSEUM_GLORY(60L),
    CALVARION(8L),
    CERBERUS(9L),
    CHAMBERS_OF_XERIC(10L),
    CHAMBERS_OF_XERIC_CHALLENGE_MODE(11L),
    CHAOS_ELEMENTAL(12L),
    CHAOS_FANATIC(13L),
    COMMANDER_ZILYANA(14L),
    CORPOREAL_BEAST(15L),
    CRAZY_ARCHAEOLOGIST(16L),
    DAGANNOTH_PRIME(17L),
    DAGANNOTH_REX(18L),
    DAGANNOTH_SUPREME(19L),
    DERANGED_ARCHAEOLOGIST(20L),
    DUKE_SUCELLUS(21L),
    GENERAL_GRAARDOR(22L),
    GIANT_MOLE(23L),
    GROTESQUE_GUARDIANS(24L),
    HESPORI(25L),
    KALPHITE_QUEEN(26L),
    KING_BLACK_DRAGON(27L),
    KRAKEN(28L),
    KREEARRA(29L),
    KRIL_TSUTSAROTH(30L),
    MIMIC(31L),
    NEX(32L),
    NIGHTMARE(33L),
    PHOSANIS_NIGHTMARE(34L),
    OBOR(35L),
    PHANTOM_MUSPAH(36L),
    SARACHNIS(37L),
    SCORPIA(38L),
    SCURRIUS(100),
    SKOTIZO(39L),
    SPINDEL(40L),
    TEMPOROSS(41L),
    THE_GAUNTLET(42L),
    THE_CORRUPTED_GAUNTLET(43L),
    THE_LEVIATHAN(44L),
    THE_WHISPERER(45L),
    THEATRE_OF_BLOOD(46L),
    THEATRE_OF_BLOOD_HARD_MODE(47L),
    THERMONUCLEAR_SMOKE_DEVIL(48L),
    TOMBS_OF_AMASCUT(49L),
    TOMBS_OF_AMASCUT_EXPERT(50L),
    TZKAL_ZUK(51L),
    TZTOK_JAD(52L),
    VARDORVIS(53L),
    VENENATIS(54L),
    VETION(55L),
    VORKATH(56L),
    WINTERTODT(57L),
    ZALCANO(58L),
    ZULRAH(59L);

    public static Boss fromId(long id) {
        return Stream.of(values())
                .filter(boss -> boss.getBossId() == id)
                .findFirst()
                .orElse(null);
    }

    private final long bossId;

    Boss(long bossId) {
        this.bossId = bossId;
    }

    public long getBossId() {
        return bossId;
    }
}
