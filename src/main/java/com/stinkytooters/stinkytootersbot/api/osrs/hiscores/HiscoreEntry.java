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
    RIFTS_CLOSED(38, 2),
    ABYSSAL_SIRE(39, 2),
    HYDRA(40, 2),
    ARTIO(41, 2),
    BARROWS(42, 2),
    BRYOPHYTA(43, 2),
    CALLISTO(44, 2),
    CALVRION(45, 2),
    CERBERUS(46, 2),
    COX(47, 2),
    COX_CHALLENGE(48, 2),
    CHAOS_ELEMENTAL(49, 2),
    CHAOS_FANATIC(50, 2),
    ZILYANA(51, 2),
    CORP(52, 2),
    CRAZY_ARCHEOLOGIST(53, 2),
    PRIME(54, 2),
    REX(55, 2),
    SUPREME(56, 2),
    DERANGED_ARCHEOLOGIST(57, 2),
    DUKE(58, 2),
    GRAADOR(59, 2),
    GIANT_MOLE(60, 2),
    GUARDIANS(61, 2),
    HESPORI(62, 2),
    KALPHITE_QUEEN(63, 2),
    KBD(64, 2),
    KRAKEN(65, 2),
    KREEARA(66, 2),
    KRIL_TSUTSAROTH(67, 2),
    MIMIC(68, 2),
    NEX(69, 2),
    NIGHTMARE(70, 2),
    PHOSANIS_NIGHTMARE(71, 2),
    OBOR(72, 2),
    PHANTOM_MUSPAH(73, 2),
    SARACHNIS(74, 2),
    SCORPIA(75, 2),
    SKOTIZO(76, 2),
    SPINDEL(77, 2),
    TEMPOROSS(78, 2),
    GAUNTLET(79, 2),
    CORRUPTED_GAUNTLET(80, 2),
    LEVIATHAN(81, 2),
    WHISPERER(82, 2),
    TOB(83, 2),
    TOB_HARD(84, 2),
    SMOKE_DEVIL(85, 2),
    TOA(86, 2),
    TOA_EXPERT(87, 2),
    ZUK(88, 2),
    JAD(89, 2),
    VARDORVIS(90, 2),
    VENNATIS(91, 2),
    VETTION(92, 2),
    VORKATH(93, 2),
    WINTERTODT(94, 2),
    ZALCANO(95, 2),
    ZULRAH(96, 2);

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
