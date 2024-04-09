package com.stinkytooters.stinkytootersbot.api.osrs.hiscores;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;

public class EHPData {
    

    private final Map<Skill, Integer> ehpRates;


    //Some skills have different rates for levels, Took the pre 99 last entry from Wiseoldman.net/ehp/ironman
    int[] rates = {
        -1, //Overall, Not calculated this way
        186000, //Attack
        768000, //Defence
        300000, //Strength
        1219000, //Ranged
        -1, //Magic "0 time" so very large 
        176000, //Prayer
        426000, //Cooking
        194000, //Woodcutting
        1531000, //Fletching
        130000, //Fishing
        356000, //Firemaking
        346000, //Crafting
        315000, //Smithing
        90000, //Mining A bit of rounding here, interesting EHB methods
        70200, //Herblore
        92000, //Agility (Post 6m xp)
        296000, //Thieving
        63000, //Slayer also slight rounding since rates change a lot
        2000000, //Farming
        119000, //Runecrafting
        240000, //Hunter
        240000, //Construction
    };

    public EHPData() {
        ehpRates = new TreeMap<>();

        int i = 0;
        for (Skill skill : Skill.values()) {
            ehpRates.put(skill, rates[i]);
            i++;
        }
    }

    public int GetEHP(Skill skill) {
        return ehpRates.get(skill);
    }

}
