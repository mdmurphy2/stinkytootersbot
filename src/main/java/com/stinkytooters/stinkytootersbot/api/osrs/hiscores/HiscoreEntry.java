package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum HiscoreEntry {
    OVERALL(10, 3),
    ATTACK(20, 3),
    DEFENCE(30, 3),
    STRENGTH(40, 3),
    HITPOINTS(50, 3),
    RANGED(60, 3),
    PRAYER(70, 3),
    MAGIC(80, 3),
    COOKING(90, 3),
    WOODCUTTING(100, 3),
    FLETCHING(110, 3),
    FISHING(120, 3),
    FIREMAKING(130, 3),
    CRAFTING(140, 3),
    SMITHING(150, 3),
    MINING(160, 3),
    HERBLORE(170, 3),
    AGILITY(180, 3),
    THIEVING(190, 3),
    SLAYER(200, 3),
    FARMING(210, 3),
    RUNECRAFT(220, 3),
    HUNTER(230, 3),
    CONSTRUCTION(240, 3),
    UNKNOWN1(250, 2),
    UNKNOWN2(260, 2),
    UNKNOWN3(270, 2),
    UNKNOWN4(280, 2),
    BOUNTY_HUNTER_LEGACY(290, 2),
    UNKNOWN5(300, 2),
    CLUE_ALL(310, 2),
    CLUE_BEGINNER(320, 2),
    CLUE_EASY(330, 2),
    CLUE_MEDIUM(340, 2),
    CLUE_HARD(350, 2),
    CLUE_ELITE(360, 2),
    CLUE_MASTER(370, 2),
    LMS_RANK(380, 2),
    UNKNOWN6(390, 2),
    SOUL_WARS_ZEAL(400, 2),
    GOTR(410, 2),
    COLOSSEUM_GLORY(420, 2),
    ABYSSAL_SIRE(430, 2),
    ALCHEMICAL_HYDRA(440, 2),
    ARTIO(450, 2),
    BARROWS_CHESTS(460, 2),
    BRYOPHYTA(470, 2),
    CALLISTO(480, 2),
    CALVARION(490, 2),
    CERBERUS(500, 2),
    CHAMBERS_OF_XERIC(510, 2),
    CHAMBERS_OF_XERIC_CHALLENGE_MODE(520, 2),
    CHAOS_ELEMENTAL(530, 2),
    CHAOS_FANATIC(540, 2),
    COMMANDER_ZILYANA(550, 2),
    CORPOREAL_BEAST(560, 2),
    CRAZY_ARCHAEOLOGIST(570, 2),
    DAGANNOTH_PRIME(580, 2),
    DAGANNOTH_REX(590, 2),
    DAGANNOTH_SUPREME(600, 2),
    DERANGED_ARCHAEOLOGIST(610, 2),
    DUKE_SUCELLUS(620, 2),
    GENERAL_GRAARDOR(630, 2),
    GIANT_MOLE(640, 2),
    GROTESQUE_GUARDIANS(650, 2),
    HESPORI(660, 2),
    KALPHITE_QUEEN(670, 2),
    KING_BLACK_DRAGON(680, 2),
    KRAKEN(690, 2),
    KREEARRA(700, 2),
    KRIL_TSUTSAROTH(710, 2),
    MIMIC(720, 2),
    NEX(730, 2),
    NIGHTMARE(740, 2),
    PHOSANIS_NIGHTMARE(750, 2),
    OBOR(760, 2),
    PHANTOM_MUSPAH(770, 2),
    SARACHNIS(780, 2),
    SCORPIA(790, 2),
    SCURRIUS(800, 2),
    SKOTIZO(810, 2),
    SPINDEL(820, 2),
    TEMPOROSS(830, 2),
    THE_GAUNTLET(840, 2),
    THE_CORRUPTED_GAUNTLET(850, 2),
    THE_LEVIATHAN(860, 2),
    THE_WHISPERER(870, 2),
    THEATRE_OF_BLOOD(880, 2),
    THEATRE_OF_BLOOD_HARD_MODE(890, 2),
    THERMONUCLEAR_SMOKE_DEVIL(900, 2),
    TOMBS_OF_AMASCUT(910, 2),
    TOMBS_OF_AMASCUT_EXPERT(920, 2),
    TZKAL_ZUK(930, 2),
    TZTOK_JAD(940, 2),
    VARDORVIS(950, 2),
    VENENATIS(960, 2),
    VETION(970, 2),
    VORKATH(980, 2),
    WINTERTODT(990, 2),
    ZALCANO(1000, 2),
    ZULRAH(1010, 2);

    private final int order;
    private final int entries;

    public static List<HiscoreEntry> getOrderedEntries() {
        return Arrays.stream(HiscoreEntry.values())
                .sorted(Comparator.comparing(HiscoreEntry::getOrder))
                .collect(Collectors.toList());
    }

    HiscoreEntry(int order, int entries) {
        this.order = order;
        this.entries = entries;
    }

    public int getOrder() {
        return order;
    }

    public int getEntries() {
        return entries;
    }

    public boolean isBoss() {
        return getEntries() == 2;
    }

    public boolean isSkill() {
        return getEntries() == 3;
    }
}
