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
