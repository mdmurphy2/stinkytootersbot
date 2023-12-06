package com.stinkytooters.stinkytootersbot.service.hiscore.data;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.Skill;

import java.sql.Timestamp;

public class HiscoreData {

    private long id;
    private long userId;
    private Timestamp updateTime;

    private int overallXp;
    private int overallRank;
    private int overallLevel;

    private int attackXp;
    private int attackRank;
    private int attackLevel;

    private int defenceXp;
    private int defenceRank;
    private int defenceLevel;

    private int strengthXp;
    private int strengthRank;
    private int strengthLevel;

    private int hitpointsXp;
    private int hitpointsRank;
    private int hitpointsLevel;

    private int rangedXp;
    private int rangedRank;
    private int rangedLevel;

    private int prayerXp;
    private int prayerRank;
    private int prayerLevel;

    private int magicXp;
    private int magicRank;
    private int magicLevel;

    private int cookingXp;
    private int cookingRank;
    private int cookingLevel;

    private int woodcuttingXp;
    private int woodcuttingRank;
    private int woodcuttingLevel;

    private int fletchingXp;
    private int fletchingRank;
    private int fletchingLevel;

    private int fishingXp;
    private int fishingRank;
    private int fishingLevel;

    private int firemakingXp;
    private int firemakingRank;
    private int firemakingLevel;

    private int craftingXp;
    private int craftingRank;
    private int craftingLevel;

    private int smithingXp;
    private int smithingRank;
    private int smithingLevel;

    private int miningXp;
    private int miningRank;
    private int miningLevel;

    private int herbloreXp;
    private int herbloreRank;
    private int herbloreLevel;

    private int agilityXp;
    private int agilityRank;
    private int agilityLevel;

    private int thievingXp;
    private int thievingRank;
    private int thievingLevel;

    private int slayerXp;
    private int slayerRank;
    private int slayerLevel;

    private int farmingXp;
    private int farmingRank;
    private int farmingLevel;

    private int runecraftXp;
    private int runecraftRank;
    private int runecraftLevel;

    private int hunterXp;
    private int hunterRank;
    private int hunterLevel;

    private int constructionXp;
    private int constructionRank;
    private int constructionLevel;
    
    private int gotrRank;
    private int gotrKc;

    private int sireRank;
    private int sireKc;

    private int hydraRank;
    private int hyrdraKc;

    private int artioRank;
    private int artioKc;

    private int barrowsRank;
    private int barrowsKc;

    private int bryophytaRank;
    private int bryophytaKc;
    
    private int callistoRank;
    private int callistoKc;

    private int calvarionRank;
    private int calvarionKc;

    private int cerberusRank;
    private int cerberusKc;

    private int coxRank;
    private int coxKc;

    private int coxcmRank;
    private int coxcmKc;

    private int ceRank;
    private int ceKc;

    private int cfRank;
    private int cfKc;

    private int zilyanaRank;
    private int zilyanaKc;

    private int corpRank;
    private int corpKc;

    private int caRank;
    private int caKc;

    private int primeRank;
    private int primeKc;

    private int rexRank;
    private int rexKc;

    private int supremeRank;
    private int supremeKc;

    private int derangedRank;
    private int derangedKc;

    private int dukeRank;
    private int dukeKc;

    private int graadrorRank;
    private int graardorKc;
    private int moleRank;

    private int moleKc;
    private int ggRank;

    private int ggKc;
    private int hesporiRank;
    private int hesporiKc;
    
    private int kqRank;
    private int kqKc;

    private int kbdRank;
    private int kbdKc;

    private int krakenRank;
    private int krakenKc;

    private int kreearraRank;
    private int kreearraKc;

    private int krilRank;
    private int krilKc;

    private int mimicRank;
    private int mimicKc;

    private int nexRank;
    private int nexKc;

    private int nightmareRank;
    private int nightmareKc;

    private int phosanisRank;
    private int phosanisKc;

    private int oborRank;
    private int oborKc;

    private int muspahRank;
    private int muspahKc;

    private int sarachnisRank;
    private int sarachnisKc;

    private int scorpiaRank;
    private int scorpiaKc;

    private int skotizoRank;
    private int skotizoKc;

    private int spindelRank;
    private int spindelKc;

    private int temporossRank;
    private int temporossKc;

    private int gauntletRank;
    private int gauntletKc;

    private int cgRank;
    private int cgKc;

    private int leviathanRank;
    private int leviathanKc;

    private int whispererRank;
    private int whispererKc;

    private int tobRank;
    private int tobKc;

    private int tobhmRank;
    private int tobhmKc;

    private int thermyRank;
    private int thermyKc;

    private int toaRank;
    private int toaKc;

    private int toaexpertRank;
    private int toaexpertKc;

    private int zukRank;
    private int zukKc;

    private int jadRank;
    private int jadKc;

    private int vardorvisRank;
    private int vardorvisKc;

    private int venenatisRank;
    private int venenatisKc;

    private int vetionRank;
    private int vetionKc;

    private int vorkathRank;
    private int vorkathKc;

    private int wintertodtRank;
    private int wintertodtKc;

    private int zalcanoRank;
    private int zalcanoKc;

    private int zulrahRank;
    private int zulrahKc;
    
    

    public Hiscore toHiscore() {
        Hiscore hiscore = new Hiscore();
        hiscore.setId(id);
        if (updateTime != null) {
            hiscore.setUpdateTime(updateTime.toInstant());
        }
        hiscore.setUserId(userId);

        hiscore.addXp(Skill.OVERALL, overallXp);
        hiscore.addXp(Skill.ATTACK, attackXp);
        hiscore.addXp(Skill.DEFENCE, defenceXp);
        hiscore.addXp(Skill.STRENGTH, strengthXp);
        hiscore.addXp(Skill.HITPOINTS, hitpointsXp);
        hiscore.addXp(Skill.RANGED, rangedXp);
        hiscore.addXp(Skill.PRAYER, prayerXp);
        hiscore.addXp(Skill.MAGIC, magicXp);
        hiscore.addXp(Skill.COOKING, cookingXp);
        hiscore.addXp(Skill.WOODCUTTING, woodcuttingXp);
        hiscore.addXp(Skill.FLETCHING, fletchingXp);
        hiscore.addXp(Skill.FISHING, fishingXp);
        hiscore.addXp(Skill.FIREMAKING, firemakingXp);
        hiscore.addXp(Skill.CRAFTING, craftingXp);
        hiscore.addXp(Skill.SMITHING, smithingXp);
        hiscore.addXp(Skill.MINING, miningXp);
        hiscore.addXp(Skill.HERBLORE, herbloreXp);
        hiscore.addXp(Skill.AGILITY, agilityXp);
        hiscore.addXp(Skill.THIEVING, thievingXp);
        hiscore.addXp(Skill.SLAYER, slayerXp);
        hiscore.addXp(Skill.FARMING, farmingXp);
        hiscore.addXp(Skill.RUNECRAFT, runecraftXp);
        hiscore.addXp(Skill.HUNTER, hunterXp);
        hiscore.addXp(Skill.CONSTRUCTION, constructionXp);


        hiscore.addLevel(Skill.OVERALL, overallLevel);
        hiscore.addLevel(Skill.ATTACK, attackLevel);
        hiscore.addLevel(Skill.DEFENCE, defenceLevel);
        hiscore.addLevel(Skill.STRENGTH, strengthLevel);
        hiscore.addLevel(Skill.HITPOINTS, hitpointsLevel);
        hiscore.addLevel(Skill.RANGED, rangedLevel);
        hiscore.addLevel(Skill.PRAYER, prayerLevel);
        hiscore.addLevel(Skill.MAGIC, magicLevel);
        hiscore.addLevel(Skill.COOKING, cookingLevel);
        hiscore.addLevel(Skill.WOODCUTTING, woodcuttingLevel);
        hiscore.addLevel(Skill.FLETCHING, fletchingLevel);
        hiscore.addLevel(Skill.FISHING, fishingLevel);
        hiscore.addLevel(Skill.FIREMAKING, firemakingLevel);
        hiscore.addLevel(Skill.CRAFTING, craftingLevel);
        hiscore.addLevel(Skill.SMITHING, smithingLevel);
        hiscore.addLevel(Skill.MINING, miningLevel);
        hiscore.addLevel(Skill.HERBLORE, herbloreLevel);
        hiscore.addLevel(Skill.AGILITY, agilityLevel);
        hiscore.addLevel(Skill.THIEVING, thievingLevel);
        hiscore.addLevel(Skill.SLAYER, slayerLevel);
        hiscore.addLevel(Skill.FARMING, farmingLevel);
        hiscore.addLevel(Skill.RUNECRAFT, runecraftLevel);
        hiscore.addLevel(Skill.HUNTER, hunterLevel);
        hiscore.addLevel(Skill.CONSTRUCTION, constructionLevel);

        hiscore.addRank(Skill.OVERALL, overallRank);
        hiscore.addRank(Skill.ATTACK, attackRank);
        hiscore.addRank(Skill.DEFENCE, defenceRank);
        hiscore.addRank(Skill.STRENGTH, strengthRank);
        hiscore.addRank(Skill.HITPOINTS, hitpointsRank);
        hiscore.addRank(Skill.RANGED, rangedRank);
        hiscore.addRank(Skill.PRAYER, prayerRank);
        hiscore.addRank(Skill.MAGIC, magicRank);
        hiscore.addRank(Skill.COOKING, cookingRank);
        hiscore.addRank(Skill.WOODCUTTING, woodcuttingRank);
        hiscore.addRank(Skill.FLETCHING, fletchingRank);
        hiscore.addRank(Skill.FISHING, fishingRank);
        hiscore.addRank(Skill.FIREMAKING, firemakingRank);
        hiscore.addRank(Skill.CRAFTING, craftingRank);
        hiscore.addRank(Skill.SMITHING, smithingRank);
        hiscore.addRank(Skill.MINING, miningRank);
        hiscore.addRank(Skill.HERBLORE, herbloreRank);
        hiscore.addRank(Skill.AGILITY, agilityRank);
        hiscore.addRank(Skill.THIEVING, thievingRank);
        hiscore.addRank(Skill.SLAYER, slayerRank);
        hiscore.addRank(Skill.FARMING, farmingRank);
        hiscore.addRank(Skill.RUNECRAFT, runecraftRank);
        hiscore.addRank(Skill.HUNTER, hunterRank);
        hiscore.addRank(Skill.CONSTRUCTION, constructionRank);

        return hiscore;
    }

    public static HiscoreData from(Hiscore hiscore) {
        HiscoreData data = new HiscoreData();
        data.setId(hiscore.getId());
        data.setUserId(hiscore.getUserId());

        if (hiscore.getUpdateTime() != null) {
            data.setUpdateTime(Timestamp.from(hiscore.getUpdateTime()));
        }

        data.setOverallXp(hiscore.getXp(Skill.OVERALL));
        data.setAttackXp(hiscore.getXp(Skill.ATTACK));
        data.setDefenceXp(hiscore.getXp(Skill.DEFENCE));
        data.setStrengthXp(hiscore.getXp(Skill.STRENGTH));
        data.setHitpointsXp(hiscore.getXp(Skill.HITPOINTS));
        data.setRangedXp(hiscore.getXp(Skill.RANGED));
        data.setPrayerXp(hiscore.getXp(Skill.PRAYER));
        data.setMagicXp(hiscore.getXp(Skill.MAGIC));
        data.setCookingXp(hiscore.getXp(Skill.COOKING));
        data.setWoodcuttingXp(hiscore.getXp(Skill.WOODCUTTING));
        data.setFletchingXp(hiscore.getXp(Skill.FLETCHING));
        data.setFishingXp(hiscore.getXp(Skill.FISHING));
        data.setFiremakingXp(hiscore.getXp(Skill.FIREMAKING));
        data.setCraftingXp(hiscore.getXp(Skill.CRAFTING));
        data.setSmithingXp(hiscore.getXp(Skill.SMITHING));
        data.setMiningXp(hiscore.getXp(Skill.MINING));
        data.setHerbloreXp(hiscore.getXp(Skill.HERBLORE));
        data.setAgilityXp(hiscore.getXp(Skill.AGILITY));
        data.setThievingXp(hiscore.getXp(Skill.THIEVING));
        data.setSlayerXp(hiscore.getXp(Skill.SLAYER));
        data.setFarmingXp(hiscore.getXp(Skill.FARMING));
        data.setRunecraftXp(hiscore.getXp(Skill.RUNECRAFT));
        data.setHunterXp(hiscore.getXp(Skill.HUNTER));
        data.setConstructionXp(hiscore.getXp(Skill.CONSTRUCTION));


        data.setOverallLevel(hiscore.getLevel(Skill.OVERALL));
        data.setAttackLevel(hiscore.getLevel(Skill.ATTACK));
        data.setDefenceLevel(hiscore.getLevel(Skill.DEFENCE));
        data.setStrengthLevel(hiscore.getLevel(Skill.STRENGTH));
        data.setHitpointsLevel(hiscore.getLevel(Skill.HITPOINTS));
        data.setRangedLevel(hiscore.getLevel(Skill.RANGED));
        data.setPrayerLevel(hiscore.getLevel(Skill.PRAYER));
        data.setMagicLevel(hiscore.getLevel(Skill.MAGIC));
        data.setCookingLevel(hiscore.getLevel(Skill.COOKING));
        data.setWoodcuttingLevel(hiscore.getLevel(Skill.WOODCUTTING));
        data.setFletchingLevel(hiscore.getLevel(Skill.FLETCHING));
        data.setFishingLevel(hiscore.getLevel(Skill.FISHING));
        data.setFiremakingLevel(hiscore.getLevel(Skill.FIREMAKING));
        data.setCraftingLevel(hiscore.getLevel(Skill.CRAFTING));
        data.setSmithingLevel(hiscore.getLevel(Skill.SMITHING));
        data.setMiningLevel(hiscore.getLevel(Skill.MINING));
        data.setHerbloreLevel(hiscore.getLevel(Skill.HERBLORE));
        data.setAgilityLevel(hiscore.getLevel(Skill.AGILITY));
        data.setThievingLevel(hiscore.getLevel(Skill.THIEVING));
        data.setSlayerLevel(hiscore.getLevel(Skill.SLAYER));
        data.setFarmingLevel(hiscore.getLevel(Skill.FARMING));
        data.setRunecraftLevel(hiscore.getLevel(Skill.RUNECRAFT));
        data.setHunterLevel(hiscore.getLevel(Skill.HUNTER));
        data.setConstructionLevel(hiscore.getLevel(Skill.CONSTRUCTION));

        data.setOverallRank(hiscore.getRank(Skill.OVERALL));
        data.setAttackRank(hiscore.getRank(Skill.ATTACK));
        data.setDefenceRank(hiscore.getRank(Skill.DEFENCE));
        data.setStrengthRank(hiscore.getRank(Skill.STRENGTH));
        data.setHitpointsRank(hiscore.getRank(Skill.HITPOINTS));
        data.setRangedRank(hiscore.getRank(Skill.RANGED));
        data.setPrayerRank(hiscore.getRank(Skill.PRAYER));
        data.setMagicRank(hiscore.getRank(Skill.MAGIC));
        data.setCookingRank(hiscore.getRank(Skill.COOKING));
        data.setWoodcuttingRank(hiscore.getRank(Skill.WOODCUTTING));
        data.setFletchingRank(hiscore.getRank(Skill.FLETCHING));
        data.setFishingRank(hiscore.getRank(Skill.FISHING));
        data.setFiremakingRank(hiscore.getRank(Skill.FIREMAKING));
        data.setCraftingRank(hiscore.getRank(Skill.CRAFTING));
        data.setSmithingRank(hiscore.getRank(Skill.SMITHING));
        data.setMiningRank(hiscore.getRank(Skill.MINING));
        data.setHerbloreRank(hiscore.getRank(Skill.HERBLORE));
        data.setAgilityRank(hiscore.getRank(Skill.AGILITY));
        data.setThievingRank(hiscore.getRank(Skill.THIEVING));
        data.setSlayerRank(hiscore.getRank(Skill.SLAYER));
        data.setFarmingRank(hiscore.getRank(Skill.FARMING));
        data.setRunecraftRank(hiscore.getRank(Skill.RUNECRAFT));
        data.setHunterRank(hiscore.getRank(Skill.HUNTER));
        data.setConstructionRank(hiscore.getRank(Skill.CONSTRUCTION));

        return data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public int getOverallXp() {
        return overallXp;
    }

    public void setOverallXp(int overallXp) {
        this.overallXp = overallXp;
    }

    public int getOverallRank() {
        return overallRank;
    }

    public void setOverallRank(int overallRank) {
        this.overallRank = overallRank;
    }

    public int getOverallLevel() {
        return overallLevel;
    }

    public void setOverallLevel(int overallLevel) {
        this.overallLevel = overallLevel;
    }

    public int getAttackXp() {
        return attackXp;
    }

    public void setAttackXp(int attackXp) {
        this.attackXp = attackXp;
    }

    public int getAttackRank() {
        return attackRank;
    }

    public void setAttackRank(int attackRank) {
        this.attackRank = attackRank;
    }

    public int getAttackLevel() {
        return attackLevel;
    }

    public void setAttackLevel(int attackLevel) {
        this.attackLevel = attackLevel;
    }

    public int getDefenceXp() {
        return defenceXp;
    }

    public void setDefenceXp(int defenceXp) {
        this.defenceXp = defenceXp;
    }

    public int getDefenceRank() {
        return defenceRank;
    }

    public void setDefenceRank(int defenceRank) {
        this.defenceRank = defenceRank;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public void setDefenceLevel(int defenceLevel) {
        this.defenceLevel = defenceLevel;
    }

    public int getStrengthXp() {
        return strengthXp;
    }

    public void setStrengthXp(int strengthXp) {
        this.strengthXp = strengthXp;
    }

    public int getStrengthRank() {
        return strengthRank;
    }

    public void setStrengthRank(int strengthRank) {
        this.strengthRank = strengthRank;
    }

    public int getStrengthLevel() {
        return strengthLevel;
    }

    public void setStrengthLevel(int strengthLevel) {
        this.strengthLevel = strengthLevel;
    }

    public int getHitpointsXp() {
        return hitpointsXp;
    }

    public void setHitpointsXp(int hitpointsXp) {
        this.hitpointsXp = hitpointsXp;
    }

    public int getHitpointsRank() {
        return hitpointsRank;
    }

    public void setHitpointsRank(int hitpointsRank) {
        this.hitpointsRank = hitpointsRank;
    }

    public int getHitpointsLevel() {
        return hitpointsLevel;
    }

    public void setHitpointsLevel(int hitpointsLevel) {
        this.hitpointsLevel = hitpointsLevel;
    }

    public int getRangedXp() {
        return rangedXp;
    }

    public void setRangedXp(int rangedXp) {
        this.rangedXp = rangedXp;
    }

    public int getRangedRank() {
        return rangedRank;
    }

    public void setRangedRank(int rangedRank) {
        this.rangedRank = rangedRank;
    }

    public int getRangedLevel() {
        return rangedLevel;
    }

    public void setRangedLevel(int rangedLevel) {
        this.rangedLevel = rangedLevel;
    }

    public int getPrayerXp() {
        return prayerXp;
    }

    public void setPrayerXp(int prayerXp) {
        this.prayerXp = prayerXp;
    }

    public int getPrayerRank() {
        return prayerRank;
    }

    public void setPrayerRank(int prayerRank) {
        this.prayerRank = prayerRank;
    }

    public int getPrayerLevel() {
        return prayerLevel;
    }

    public void setPrayerLevel(int prayerLevel) {
        this.prayerLevel = prayerLevel;
    }

    public int getMagicXp() {
        return magicXp;
    }

    public void setMagicXp(int magicXp) {
        this.magicXp = magicXp;
    }

    public int getMagicRank() {
        return magicRank;
    }

    public void setMagicRank(int magicRank) {
        this.magicRank = magicRank;
    }

    public int getMagicLevel() {
        return magicLevel;
    }

    public void setMagicLevel(int magicLevel) {
        this.magicLevel = magicLevel;
    }

    public int getCookingXp() {
        return cookingXp;
    }

    public void setCookingXp(int cookingXp) {
        this.cookingXp = cookingXp;
    }

    public int getCookingRank() {
        return cookingRank;
    }

    public void setCookingRank(int cookingRank) {
        this.cookingRank = cookingRank;
    }

    public int getCookingLevel() {
        return cookingLevel;
    }

    public void setCookingLevel(int cookingLevel) {
        this.cookingLevel = cookingLevel;
    }

    public int getWoodcuttingXp() {
        return woodcuttingXp;
    }

    public void setWoodcuttingXp(int woodcuttingXp) {
        this.woodcuttingXp = woodcuttingXp;
    }

    public int getWoodcuttingRank() {
        return woodcuttingRank;
    }

    public void setWoodcuttingRank(int woodcuttingRank) {
        this.woodcuttingRank = woodcuttingRank;
    }

    public int getWoodcuttingLevel() {
        return woodcuttingLevel;
    }

    public void setWoodcuttingLevel(int woodcuttingLevel) {
        this.woodcuttingLevel = woodcuttingLevel;
    }

    public int getFletchingXp() {
        return fletchingXp;
    }

    public void setFletchingXp(int fletchingXp) {
        this.fletchingXp = fletchingXp;
    }

    public int getFletchingRank() {
        return fletchingRank;
    }

    public void setFletchingRank(int fletchingRank) {
        this.fletchingRank = fletchingRank;
    }

    public int getFletchingLevel() {
        return fletchingLevel;
    }

    public void setFletchingLevel(int fletchingLevel) {
        this.fletchingLevel = fletchingLevel;
    }

    public int getFishingXp() {
        return fishingXp;
    }

    public void setFishingXp(int fishingXp) {
        this.fishingXp = fishingXp;
    }

    public int getFishingRank() {
        return fishingRank;
    }

    public void setFishingRank(int fishingRank) {
        this.fishingRank = fishingRank;
    }

    public int getFishingLevel() {
        return fishingLevel;
    }

    public void setFishingLevel(int fishingLevel) {
        this.fishingLevel = fishingLevel;
    }

    public int getFiremakingXp() {
        return firemakingXp;
    }

    public void setFiremakingXp(int firemakingXp) {
        this.firemakingXp = firemakingXp;
    }

    public int getFiremakingRank() {
        return firemakingRank;
    }

    public void setFiremakingRank(int firemakingRank) {
        this.firemakingRank = firemakingRank;
    }

    public int getFiremakingLevel() {
        return firemakingLevel;
    }

    public void setFiremakingLevel(int firemakingLevel) {
        this.firemakingLevel = firemakingLevel;
    }

    public int getCraftingXp() {
        return craftingXp;
    }

    public void setCraftingXp(int craftingXp) {
        this.craftingXp = craftingXp;
    }

    public int getCraftingRank() {
        return craftingRank;
    }

    public void setCraftingRank(int craftingRank) {
        this.craftingRank = craftingRank;
    }

    public int getCraftingLevel() {
        return craftingLevel;
    }

    public void setCraftingLevel(int craftingLevel) {
        this.craftingLevel = craftingLevel;
    }

    public int getSmithingXp() {
        return smithingXp;
    }

    public void setSmithingXp(int smithingXp) {
        this.smithingXp = smithingXp;
    }

    public int getSmithingRank() {
        return smithingRank;
    }

    public void setSmithingRank(int smithingRank) {
        this.smithingRank = smithingRank;
    }

    public int getSmithingLevel() {
        return smithingLevel;
    }

    public void setSmithingLevel(int smithingLevel) {
        this.smithingLevel = smithingLevel;
    }

    public int getMiningXp() {
        return miningXp;
    }

    public void setMiningXp(int miningXp) {
        this.miningXp = miningXp;
    }

    public int getMiningRank() {
        return miningRank;
    }

    public void setMiningRank(int miningRank) {
        this.miningRank = miningRank;
    }

    public int getMiningLevel() {
        return miningLevel;
    }

    public void setMiningLevel(int miningLevel) {
        this.miningLevel = miningLevel;
    }

    public int getHerbloreXp() {
        return herbloreXp;
    }

    public void setHerbloreXp(int herbloreXp) {
        this.herbloreXp = herbloreXp;
    }

    public int getHerbloreRank() {
        return herbloreRank;
    }

    public void setHerbloreRank(int herbloreRank) {
        this.herbloreRank = herbloreRank;
    }

    public int getHerbloreLevel() {
        return herbloreLevel;
    }

    public void setHerbloreLevel(int herbloreLevel) {
        this.herbloreLevel = herbloreLevel;
    }

    public int getAgilityXp() {
        return agilityXp;
    }

    public void setAgilityXp(int agilityXp) {
        this.agilityXp = agilityXp;
    }

    public int getAgilityRank() {
        return agilityRank;
    }

    public void setAgilityRank(int agilityRank) {
        this.agilityRank = agilityRank;
    }

    public int getAgilityLevel() {
        return agilityLevel;
    }

    public void setAgilityLevel(int agilityLevel) {
        this.agilityLevel = agilityLevel;
    }

    public int getThievingXp() {
        return thievingXp;
    }

    public void setThievingXp(int thievingXp) {
        this.thievingXp = thievingXp;
    }

    public int getThievingRank() {
        return thievingRank;
    }

    public void setThievingRank(int thievingRank) {
        this.thievingRank = thievingRank;
    }

    public int getThievingLevel() {
        return thievingLevel;
    }

    public void setThievingLevel(int thievingLevel) {
        this.thievingLevel = thievingLevel;
    }

    public int getSlayerXp() {
        return slayerXp;
    }

    public void setSlayerXp(int slayerXp) {
        this.slayerXp = slayerXp;
    }

    public int getSlayerRank() {
        return slayerRank;
    }

    public void setSlayerRank(int slayerRank) {
        this.slayerRank = slayerRank;
    }

    public int getSlayerLevel() {
        return slayerLevel;
    }

    public void setSlayerLevel(int slayerLevel) {
        this.slayerLevel = slayerLevel;
    }

    public int getFarmingXp() {
        return farmingXp;
    }

    public void setFarmingXp(int farmingXp) {
        this.farmingXp = farmingXp;
    }

    public int getFarmingRank() {
        return farmingRank;
    }

    public void setFarmingRank(int farmingRank) {
        this.farmingRank = farmingRank;
    }

    public int getFarmingLevel() {
        return farmingLevel;
    }

    public void setFarmingLevel(int farmingLevel) {
        this.farmingLevel = farmingLevel;
    }

    public int getRunecraftXp() {
        return runecraftXp;
    }

    public void setRunecraftXp(int runecraftXp) {
        this.runecraftXp = runecraftXp;
    }

    public int getRunecraftRank() {
        return runecraftRank;
    }

    public void setRunecraftRank(int runecraftRank) {
        this.runecraftRank = runecraftRank;
    }

    public int getRunecraftLevel() {
        return runecraftLevel;
    }

    public void setRunecraftLevel(int runecraftLevel) {
        this.runecraftLevel = runecraftLevel;
    }

    public int getHunterXp() {
        return hunterXp;
    }

    public void setHunterXp(int hunterXp) {
        this.hunterXp = hunterXp;
    }

    public int getHunterRank() {
        return hunterRank;
    }

    public void setHunterRank(int hunterRank) {
        this.hunterRank = hunterRank;
    }

    public int getHunterLevel() {
        return hunterLevel;
    }

    public void setHunterLevel(int hunterLevel) {
        this.hunterLevel = hunterLevel;
    }

    public int getConstructionXp() {
        return constructionXp;
    }

    public void setConstructionXp(int constructionXp) {
        this.constructionXp = constructionXp;
    }

    public int getConstructionRank() {
        return constructionRank;
    }

    public void setConstructionRank(int constructionRank) {
        this.constructionRank = constructionRank;
    }

    public int getConstructionLevel() {
        return constructionLevel;
    }

    public void setConstructionLevel(int constructionLevel) {
        this.constructionLevel = constructionLevel;
    }

    public int getGotrRank() {
        return gotrRank;
    }

    public void setGotrRank(int gotrRank) {
        this.gotrRank = gotrRank;
    }

    public int getGotrKc() {
        return gotrKc;
    }

    public void setGotrKc(int gotrKc) {
        this.gotrKc = gotrKc;
    }

    public int getSireRank() {
        return sireRank;
    }

    public void setSireRank(int sireRank) {
        this.sireRank = sireRank;
    }

    public int getSireKc() {
        return sireKc;
    }

    public void setSireKc(int sireKc) {
        this.sireKc = sireKc;
    }

    public int getHydraRank() {
        return hydraRank;
    }

    public void setHydraRank(int hydraRank) {
        this.hydraRank = hydraRank;
    }

    public int getHyrdraKc() {
        return hyrdraKc;
    }

    public void setHyrdraKc(int hyrdraKc) {
        this.hyrdraKc = hyrdraKc;
    }

    public int getArtioRank() {
        return artioRank;
    }

    public void setArtioRank(int artioRank) {
        this.artioRank = artioRank;
    }

    public int getArtioKc() {
        return artioKc;
    }

    public void setArtioKc(int artioKc) {
        this.artioKc = artioKc;
    }

    public int getBarrowsRank() {
        return barrowsRank;
    }

    public void setBarrowsRank(int barrowsRank) {
        this.barrowsRank = barrowsRank;
    }

    public int getBarrowsKc() {
        return barrowsKc;
    }

    public void setBarrowsKc(int barrowsKc) {
        this.barrowsKc = barrowsKc;
    }

    public int getBryophytaRank() {
        return bryophytaRank;
    }

    public void setBryophytaRank(int bryophytaRank) {
        this.bryophytaRank = bryophytaRank;
    }

    public int getBryophytaKc() {
        return bryophytaKc;
    }

    public void setBryophytaKc(int bryophytaKc) {
        this.bryophytaKc = bryophytaKc;
    }

    public int getCallistoRank() {
        return callistoRank;
    }

    public void setCallistoRank(int callistoRank) {
        this.callistoRank = callistoRank;
    }

    public int getCallistoKc() {
        return callistoKc;
    }

    public void setCallistoKc(int callistoKc) {
        this.callistoKc = callistoKc;
    }

    public int getCalvarionRank() {
        return calvarionRank;
    }

    public void setCalvarionRank(int calvarionRank) {
        this.calvarionRank = calvarionRank;
    }

    public int getCalvarionKc() {
        return calvarionKc;
    }

    public void setCalvarionKc(int calvarionKc) {
        this.calvarionKc = calvarionKc;
    }

    public int getCerberusRank() {
        return cerberusRank;
    }

    public void setCerberusRank(int cerberusRank) {
        this.cerberusRank = cerberusRank;
    }

    public int getCerberusKc() {
        return cerberusKc;
    }

    public void setCerberusKc(int cerberusKc) {
        this.cerberusKc = cerberusKc;
    }

    public int getCoxRank() {
        return coxRank;
    }

    public void setCoxRank(int coxRank) {
        this.coxRank = coxRank;
    }

    public int getCoxKc() {
        return coxKc;
    }

    public void setCoxKc(int coxKc) {
        this.coxKc = coxKc;
    }

    public int getCoxcmRank() {
        return coxcmRank;
    }

    public void setCoxcmRank(int coxcmRank) {
        this.coxcmRank = coxcmRank;
    }

    public int getCoxcmKc() {
        return coxcmKc;
    }

    public void setCoxcmKc(int coxcmKc) {
        this.coxcmKc = coxcmKc;
    }

    public int getCeRank() {
        return ceRank;
    }

    public void setCeRank(int ceRank) {
        this.ceRank = ceRank;
    }

    public int getCeKc() {
        return ceKc;
    }

    public void setCeKc(int ceKc) {
        this.ceKc = ceKc;
    }

    public int getCfRank() {
        return cfRank;
    }

    public void setCfRank(int cfRank) {
        this.cfRank = cfRank;
    }

    public int getCfKc() {
        return cfKc;
    }

    public void setCfKc(int cfKc) {
        this.cfKc = cfKc;
    }

    public int getZilyanaRank() {
        return zilyanaRank;
    }

    public void setZilyanaRank(int zilyanaRank) {
        this.zilyanaRank = zilyanaRank;
    }

    public int getZilyanaKc() {
        return zilyanaKc;
    }

    public void setZilyanaKc(int zilyanaKc) {
        this.zilyanaKc = zilyanaKc;
    }

    public int getCorpRank() {
        return corpRank;
    }

    public void setCorpRank(int corpRank) {
        this.corpRank = corpRank;
    }

    public int getCorpKc() {
        return corpKc;
    }

    public void setCorpKc(int corpKc) {
        this.corpKc = corpKc;
    }

    public int getCaRank() {
        return caRank;
    }

    public void setCaRank(int caRank) {
        this.caRank = caRank;
    }

    public int getCaKc() {
        return caKc;
    }

    public void setCaKc(int caKc) {
        this.caKc = caKc;
    }

    public int getPrimeRank() {
        return primeRank;
    }

    public void setPrimeRank(int primeRank) {
        this.primeRank = primeRank;
    }

    public int getPrimeKc() {
        return primeKc;
    }

    public void setPrimeKc(int primeKc) {
        this.primeKc = primeKc;
    }

    public int getRexRank() {
        return rexRank;
    }

    public void setRexRank(int rexRank) {
        this.rexRank = rexRank;
    }

    public int getRexKc() {
        return rexKc;
    }

    public void setRexKc(int rexKc) {
        this.rexKc = rexKc;
    }

    public int getSupremeRank() {
        return supremeRank;
    }

    public void setSupremeRank(int supremeRank) {
        this.supremeRank = supremeRank;
    }

    public int getSupremeKc() {
        return supremeKc;
    }

    public void setSupremeKc(int supremeKc) {
        this.supremeKc = supremeKc;
    }

    public int getDerangedRank() {
        return derangedRank;
    }

    public void setDerangedRank(int derangedRank) {
        this.derangedRank = derangedRank;
    }

    public int getDerangedKc() {
        return derangedKc;
    }

    public void setDerangedKc(int derangedKc) {
        this.derangedKc = derangedKc;
    }

    public int getDukeRank() {
        return dukeRank;
    }

    public void setDukeRank(int dukeRank) {
        this.dukeRank = dukeRank;
    }

    public int getDukeKc() {
        return dukeKc;
    }

    public void setDukeKc(int dukeKc) {
        this.dukeKc = dukeKc;
    }

    public int getGraadrorRank() {
        return graadrorRank;
    }

    public void setGraadrorRank(int graadrorRank) {
        this.graadrorRank = graadrorRank;
    }

    public int getGraardorKc() {
        return graardorKc;
    }

    public void setGraardorKc(int graardorKc) {
        this.graardorKc = graardorKc;
    }

    public int getMoleRank() {
        return moleRank;
    }

    public void setMoleRank(int moleRank) {
        this.moleRank = moleRank;
    }

    public int getMoleKc() {
        return moleKc;
    }

    public void setMoleKc(int moleKc) {
        this.moleKc = moleKc;
    }

    public int getGgRank() {
        return ggRank;
    }

    public void setGgRank(int ggRank) {
        this.ggRank = ggRank;
    }

    public int getGgKc() {
        return ggKc;
    }

    public void setGgKc(int ggKc) {
        this.ggKc = ggKc;
    }

    public int getHesporiRank() {
        return hesporiRank;
    }

    public void setHesporiRank(int hesporiRank) {
        this.hesporiRank = hesporiRank;
    }

    public int getHesporiKc() {
        return hesporiKc;
    }

    public void setHesporiKc(int hesporiKc) {
        this.hesporiKc = hesporiKc;
    }

    public int getKqRank() {
        return kqRank;
    }

    public void setKqRank(int kqRank) {
        this.kqRank = kqRank;
    }

    public int getKqKc() {
        return kqKc;
    }

    public void setKqKc(int kqKc) {
        this.kqKc = kqKc;
    }

    public int getKbdRank() {
        return kbdRank;
    }

    public void setKbdRank(int kbdRank) {
        this.kbdRank = kbdRank;
    }

    public int getKbdKc() {
        return kbdKc;
    }

    public void setKbdKc(int kbdKc) {
        this.kbdKc = kbdKc;
    }

    public int getKrakenRank() {
        return krakenRank;
    }

    public void setKrakenRank(int krakenRank) {
        this.krakenRank = krakenRank;
    }

    public int getKrakenKc() {
        return krakenKc;
    }

    public void setKrakenKc(int krakenKc) {
        this.krakenKc = krakenKc;
    }

    public int getKreearraRank() {
        return kreearraRank;
    }

    public void setKreearraRank(int kreearraRank) {
        this.kreearraRank = kreearraRank;
    }

    public int getKreearraKc() {
        return kreearraKc;
    }

    public void setKreearraKc(int kreearraKc) {
        this.kreearraKc = kreearraKc;
    }

    public int getKrilRank() {
        return krilRank;
    }

    public void setKrilRank(int krilRank) {
        this.krilRank = krilRank;
    }

    public int getKrilKc() {
        return krilKc;
    }

    public void setKrilKc(int krilKc) {
        this.krilKc = krilKc;
    }

    public int getMimicRank() {
        return mimicRank;
    }

    public void setMimicRank(int mimicRank) {
        this.mimicRank = mimicRank;
    }

    public int getMimicKc() {
        return mimicKc;
    }

    public void setMimicKc(int mimicKc) {
        this.mimicKc = mimicKc;
    }

    public int getNexRank() {
        return nexRank;
    }

    public void setNexRank(int nexRank) {
        this.nexRank = nexRank;
    }

    public int getNexKc() {
        return nexKc;
    }

    public void setNexKc(int nexKc) {
        this.nexKc = nexKc;
    }

    public int getNightmareRank() {
        return nightmareRank;
    }

    public void setNightmareRank(int nightmareRank) {
        this.nightmareRank = nightmareRank;
    }

    public int getNightmareKc() {
        return nightmareKc;
    }

    public void setNightmareKc(int nightmareKc) {
        this.nightmareKc = nightmareKc;
    }

    public int getPhosanisRank() {
        return phosanisRank;
    }

    public void setPhosanisRank(int phosanisRank) {
        this.phosanisRank = phosanisRank;
    }

    public int getPhosanisKc() {
        return phosanisKc;
    }

    public void setPhosanisKc(int phosanisKc) {
        this.phosanisKc = phosanisKc;
    }

    public int getOborRank() {
        return oborRank;
    }

    public void setOborRank(int oborRank) {
        this.oborRank = oborRank;
    }

    public int getOborKc() {
        return oborKc;
    }

    public void setOborKc(int oborKc) {
        this.oborKc = oborKc;
    }

    public int getMuspahRank() {
        return muspahRank;
    }

    public void setMuspahRank(int muspahRank) {
        this.muspahRank = muspahRank;
    }

    public int getMuspahKc() {
        return muspahKc;
    }

    public void setMuspahKc(int muspahKc) {
        this.muspahKc = muspahKc;
    }

    public int getSarachnisRank() {
        return sarachnisRank;
    }

    public void setSarachnisRank(int sarachnisRank) {
        this.sarachnisRank = sarachnisRank;
    }

    public int getSarachnisKc() {
        return sarachnisKc;
    }

    public void setSarachnisKc(int sarachnisKc) {
        this.sarachnisKc = sarachnisKc;
    }

    public int getScorpiaRank() {
        return scorpiaRank;
    }

    public void setScorpiaRank(int scorpiaRank) {
        this.scorpiaRank = scorpiaRank;
    }

    public int getScorpiaKc() {
        return scorpiaKc;
    }

    public void setScorpiaKc(int scorpiaKc) {
        this.scorpiaKc = scorpiaKc;
    }

    public int getSkotizoRank() {
        return skotizoRank;
    }

    public void setSkotizoRank(int skotizoRank) {
        this.skotizoRank = skotizoRank;
    }

    public int getSkotizoKc() {
        return skotizoKc;
    }

    public void setSkotizoKc(int skotizoKc) {
        this.skotizoKc = skotizoKc;
    }

    public int getSpindelRank() {
        return spindelRank;
    }

    public void setSpindelRank(int spindelRank) {
        this.spindelRank = spindelRank;
    }

    public int getSpindelKc() {
        return spindelKc;
    }

    public void setSpindelKc(int spindelKc) {
        this.spindelKc = spindelKc;
    }

    public int getTemporossRank() {
        return temporossRank;
    }

    public void setTemporossRank(int temporossRank) {
        this.temporossRank = temporossRank;
    }

    public int getTemporossKc() {
        return temporossKc;
    }

    public void setTemporossKc(int temporossKc) {
        this.temporossKc = temporossKc;
    }

    public int getGauntletRank() {
        return gauntletRank;
    }

    public void setGauntletRank(int gauntletRank) {
        this.gauntletRank = gauntletRank;
    }

    public int getGauntletKc() {
        return gauntletKc;
    }

    public void setGauntletKc(int gauntletKc) {
        this.gauntletKc = gauntletKc;
    }

    public int getCgRank() {
        return cgRank;
    }

    public void setCgRank(int cgRank) {
        this.cgRank = cgRank;
    }

    public int getCgKc() {
        return cgKc;
    }

    public void setCgKc(int cgKc) {
        this.cgKc = cgKc;
    }

    public int getLeviathanRank() {
        return leviathanRank;
    }

    public void setLeviathanRank(int leviathanRank) {
        this.leviathanRank = leviathanRank;
    }

    public int getLeviathanKc() {
        return leviathanKc;
    }

    public void setLeviathanKc(int leviathanKc) {
        this.leviathanKc = leviathanKc;
    }

    public int getWhispererRank() {
        return whispererRank;
    }

    public void setWhispererRank(int whispererRank) {
        this.whispererRank = whispererRank;
    }

    public int getWhispererKc() {
        return whispererKc;
    }

    public void setWhispererKc(int whispererKc) {
        this.whispererKc = whispererKc;
    }

    public int getTobRank() {
        return tobRank;
    }

    public void setTobRank(int tobRank) {
        this.tobRank = tobRank;
    }

    public int getTobKc() {
        return tobKc;
    }

    public void setTobKc(int tobKc) {
        this.tobKc = tobKc;
    }

    public int getTobhmRank() {
        return tobhmRank;
    }

    public void setTobhmRank(int tobhmRank) {
        this.tobhmRank = tobhmRank;
    }

    public int getTobhmKc() {
        return tobhmKc;
    }

    public void setTobhmKc(int tobhmKc) {
        this.tobhmKc = tobhmKc;
    }

    public int getThermyRank() {
        return thermyRank;
    }

    public void setThermyRank(int thermyRank) {
        this.thermyRank = thermyRank;
    }

    public int getThermyKc() {
        return thermyKc;
    }

    public void setThermyKc(int thermyKc) {
        this.thermyKc = thermyKc;
    }

    public int getToaRank() {
        return toaRank;
    }

    public void setToaRank(int toaRank) {
        this.toaRank = toaRank;
    }

    public int getToaKc() {
        return toaKc;
    }

    public void setToaKc(int toaKc) {
        this.toaKc = toaKc;
    }

    public int getToaexpertRank() {
        return toaexpertRank;
    }

    public void setToaexpertRank(int toaexpertRank) {
        this.toaexpertRank = toaexpertRank;
    }

    public int getToaexpertKc() {
        return toaexpertKc;
    }

    public void setToaexpertKc(int toaexpertKc) {
        this.toaexpertKc = toaexpertKc;
    }

    public int getZukRank() {
        return zukRank;
    }

    public void setZukRank(int zukRank) {
        this.zukRank = zukRank;
    }

    public int getZukKc() {
        return zukKc;
    }

    public void setZukKc(int zukKc) {
        this.zukKc = zukKc;
    }

    public int getJadRank() {
        return jadRank;
    }

    public void setJadRank(int jadRank) {
        this.jadRank = jadRank;
    }

    public int getJadKc() {
        return jadKc;
    }

    public void setJadKc(int jadKc) {
        this.jadKc = jadKc;
    }

    public int getVardorvisRank() {
        return vardorvisRank;
    }

    public void setVardorvisRank(int vardorvisRank) {
        this.vardorvisRank = vardorvisRank;
    }

    public int getVardorvisKc() {
        return vardorvisKc;
    }

    public void setVardorvisKc(int vardorvisKc) {
        this.vardorvisKc = vardorvisKc;
    }

    public int getVenenatisRank() {
        return venenatisRank;
    }

    public void setVenenatisRank(int venenatisRank) {
        this.venenatisRank = venenatisRank;
    }

    public int getVenenatisKc() {
        return venenatisKc;
    }

    public void setVenenatisKc(int venenatisKc) {
        this.venenatisKc = venenatisKc;
    }

    public int getVetionRank() {
        return vetionRank;
    }

    public void setVetionRank(int vetionRank) {
        this.vetionRank = vetionRank;
    }

    public int getVetionKc() {
        return vetionKc;
    }

    public void setVetionKc(int vetionKc) {
        this.vetionKc = vetionKc;
    }

    public int getVorkathRank() {
        return vorkathRank;
    }

    public void setVorkathRank(int vorkathRank) {
        this.vorkathRank = vorkathRank;
    }

    public int getVorkathKc() {
        return vorkathKc;
    }

    public void setVorkathKc(int vorkathKc) {
        this.vorkathKc = vorkathKc;
    }

    public int getWintertodtRank() {
        return wintertodtRank;
    }

    public void setWintertodtRank(int wintertodtRank) {
        this.wintertodtRank = wintertodtRank;
    }

    public int getWintertodtKc() {
        return wintertodtKc;
    }

    public void setWintertodtKc(int wintertodtKc) {
        this.wintertodtKc = wintertodtKc;
    }

    public int getZalcanoRank() {
        return zalcanoRank;
    }

    public void setZalcanoRank(int zalcanoRank) {
        this.zalcanoRank = zalcanoRank;
    }

    public int getZalcanoKc() {
        return zalcanoKc;
    }

    public void setZalcanoKc(int zalcanoKc) {
        this.zalcanoKc = zalcanoKc;
    }

    public int getZulrahRank() {
        return zulrahRank;
    }

    public void setZulrahRank(int zulrahRank) {
        this.zulrahRank = zulrahRank;
    }

    public int getZulrahKc() {
        return zulrahKc;
    }

    public void setZulrahKc(int zulrahKc) {
        this.zulrahKc = zulrahKc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("HiscoreData{");
        sb.append("id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", overallXp=").append(overallXp);
        sb.append(", overallRank=").append(overallRank);
        sb.append(", overallLevel=").append(overallLevel);
        sb.append(", attackXp=").append(attackXp);
        sb.append(", attackRank=").append(attackRank);
        sb.append(", attackLevel=").append(attackLevel);
        sb.append(", defenceXp=").append(defenceXp);
        sb.append(", defenceRank=").append(defenceRank);
        sb.append(", defenceLevel=").append(defenceLevel);
        sb.append(", strengthXp=").append(strengthXp);
        sb.append(", strengthRank=").append(strengthRank);
        sb.append(", strengthLevel=").append(strengthLevel);
        sb.append(", hitpointsXp=").append(hitpointsXp);
        sb.append(", hitpointsRank=").append(hitpointsRank);
        sb.append(", hitpointsLevel=").append(hitpointsLevel);
        sb.append(", rangedXp=").append(rangedXp);
        sb.append(", rangedRank=").append(rangedRank);
        sb.append(", rangedLevel=").append(rangedLevel);
        sb.append(", prayerXp=").append(prayerXp);
        sb.append(", prayerRank=").append(prayerRank);
        sb.append(", prayerLevel=").append(prayerLevel);
        sb.append(", magicXp=").append(magicXp);
        sb.append(", magicRank=").append(magicRank);
        sb.append(", magicLevel=").append(magicLevel);
        sb.append(", cookingXp=").append(cookingXp);
        sb.append(", cookingRank=").append(cookingRank);
        sb.append(", cookingLevel=").append(cookingLevel);
        sb.append(", woodcuttingXp=").append(woodcuttingXp);
        sb.append(", woodcuttingRank=").append(woodcuttingRank);
        sb.append(", woodcuttingLevel=").append(woodcuttingLevel);
        sb.append(", fletchingXp=").append(fletchingXp);
        sb.append(", fletchingRank=").append(fletchingRank);
        sb.append(", fletchingLevel=").append(fletchingLevel);
        sb.append(", fishingXp=").append(fishingXp);
        sb.append(", fishingRank=").append(fishingRank);
        sb.append(", fishingLevel=").append(fishingLevel);
        sb.append(", firemakingXp=").append(firemakingXp);
        sb.append(", firemakingRank=").append(firemakingRank);
        sb.append(", firemakingLevel=").append(firemakingLevel);
        sb.append(", craftingXp=").append(craftingXp);
        sb.append(", craftingRank=").append(craftingRank);
        sb.append(", craftingLevel=").append(craftingLevel);
        sb.append(", smithingXp=").append(smithingXp);
        sb.append(", smithingRank=").append(smithingRank);
        sb.append(", smithingLevel=").append(smithingLevel);
        sb.append(", miningXp=").append(miningXp);
        sb.append(", miningRank=").append(miningRank);
        sb.append(", miningLevel=").append(miningLevel);
        sb.append(", herbloreXp=").append(herbloreXp);
        sb.append(", herbloreRank=").append(herbloreRank);
        sb.append(", herbloreLevel=").append(herbloreLevel);
        sb.append(", agilityXp=").append(agilityXp);
        sb.append(", agilityRank=").append(agilityRank);
        sb.append(", agilityLevel=").append(agilityLevel);
        sb.append(", thievingXp=").append(thievingXp);
        sb.append(", thievingRank=").append(thievingRank);
        sb.append(", thievingLevel=").append(thievingLevel);
        sb.append(", slayerXp=").append(slayerXp);
        sb.append(", slayerRank=").append(slayerRank);
        sb.append(", slayerLevel=").append(slayerLevel);
        sb.append(", farmingXp=").append(farmingXp);
        sb.append(", farmingRank=").append(farmingRank);
        sb.append(", farmingLevel=").append(farmingLevel);
        sb.append(", runecraftXp=").append(runecraftXp);
        sb.append(", runecraftRank=").append(runecraftRank);
        sb.append(", runecraftLevel=").append(runecraftLevel);
        sb.append(", hunterXp=").append(hunterXp);
        sb.append(", hunterRank=").append(hunterRank);
        sb.append(", hunterLevel=").append(hunterLevel);
        sb.append(", constructionXp=").append(constructionXp);
        sb.append(", constructionRank=").append(constructionRank);
        sb.append(", constructionLevel=").append(constructionLevel);
        sb.append('}');
        return sb.toString();
    }
}
