package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum HiscoreEntry {
    OVERALL(1, 3),
    ATTACK(2, 3),
    DEFENCE(3, 3),
    STRENGTH(4, 3),
    HITPOINTS(5, 3),
    RANGED(6, 3),
    PRAYER(7, 3),
    MAGIC(8, 3),
    COOKING(9, 3),
    WOODCUTTING(10, 3),
    FLETCHING(11, 3),
    FISHING(12, 3),
    FIREMAKING(13, 3),
    CRAFTING(14, 3),
    SMITHING(15, 3),
    MINING(16, 3),
    HERBLORE(17, 3),
    AGILITY(18, 3),
    THIEVING(19, 3),
    SLAYER(20, 3),
    FARMING(21, 3),
    RUNECRAFT(22, 3),
    HUNTER(23, 3),
    CONSTRUCTION(24, 3),
    UNKNOWN1(25, 2),
    UNKNOWN2(25, 2),
    UNKNOWN3(25, 2),
    UNKNOWN4(25, 2),
    BOUNTY_HUNTER_LEGACY(26, 2),
    UNKNOWN5(27, 2),
    CLUE_ALL(28, 2),
    CLUE_BEGINNER(29, 2),
    CLUE_EASY(30, 2),
    CLUE_MEDIUM(31, 2),
    CLUE_HARD(32, 2),
    CLUE_ELITE(33, 2),
    CLUE_MASTER(34, 2),
    LMS_RANK(35, 2),
    UNKNOWN6(36, 2),
    SOUL_WARS_ZEAL(37, 2),
    GOTR(38, 2),
    ABYSSAL_SIRE(39, 2),
    ALCHEMICAL_HYDRA(40, 2),
    ARTIO(41, 2),
    BARROWS_CHESTS(42, 2),
    BRYOPHYTA(43, 2),
    CALLISTO(44, 2),
    CALVARION(45, 2),
    CERBERUS(46, 2),
    CHAMBERS_OF_XERIC(47, 2),
    CHAMBERS_OF_XERIC_CHALLENGE_MODE(48, 2),
    CHAOS_ELEMENTAL(49, 2),
    CHAOS_FANATIC(50, 2),
    COMMANDER_ZILYANA(51, 2),
    CORPOREAL_BEAST(52, 2),
    CRAZY_ARCHAEOLOGIST(53, 2),
    DAGANNOTH_PRIME(54, 2),
    DAGANNOTH_REX(55, 2),
    DAGANNOTH_SUPREME(56, 2),
    DERANGED_ARCHAEOLOGIST(57, 2),
    DUKE_SUCELLUS(58, 2),
    GENERAL_GRAARDOR(59, 2),
    GIANT_MOLE(60, 2),
    GROTESQUE_GUARDIANS(61, 2),
    HESPORI(62, 2),
    KALPHITE_QUEEN(63, 2),
    KING_BLACK_DRAGON(64, 2),
    KRAKEN(65, 2),
    KREEARRA(66, 2),
    KRIL_TSUTSAROTH(67, 2),
    MIMIC(68, 2),
    NEX(69, 2),
    NIGHTMARE(70, 2),
    PHOSANIS_NIGHTMARE(71, 2),
    OBOR(72, 2),
    PHANTOM_MUSPAH(73, 2),
    SARACHNIS(74, 2),
    SCORPIA(75, 2),
    SCURRIUS(76, 2),
    SKOTIZO(77, 2),
    SPINDEL(78, 2),
    TEMPOROSS(79, 2),
    THE_GAUNTLET(80, 2),
    THE_CORRUPTED_GAUNTLET(81, 2),
    THE_LEVIATHAN(82, 2),
    THE_WHISPERER(83, 2),
    THEATRE_OF_BLOOD(83, 2),
    THEATRE_OF_BLOOD_HARD_MODE(84, 2),
    THERMONUCLEAR_SMOKE_DEVIL(86, 2),
    TOMBS_OF_AMASCUT(87, 2),
    TOMBS_OF_AMASCUT_EXPERT(88, 2),
    TZKAL_ZUK(89, 2),
    TZTOK_JAD(90, 2),
    VARDORVIS(91, 2),
    VENENATIS(92, 2),
    VETION(93, 2),
    VORKATH(94, 2),
    WINTERTODT(95, 2),
    ZALCANO(96, 2),
    ZULRAH(97, 2);

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
