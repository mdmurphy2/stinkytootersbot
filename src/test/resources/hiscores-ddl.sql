set schema 'stinky_tooters';

drop table if exists hiscores;
drop sequence if exists his_seq;

create sequence stinky_tooters.seq_his;

create table stinky_tooters.hiscores (
    his_id                 numeric(15) primary key default nextval('stinky_tooters.seq_his'),
    his_userid             numeric(15) not null references stinky_tooters.users(usr_id),
    his_update_time        timestamp   not null,

    his_overall_xp         numeric(10) not null,
    his_overall_rank       numeric(10) not null,
    his_overall_level      numeric(5)  not null,

    his_attack_xp          numeric(10) not null,
    his_attack_rank        numeric(10) not null,
    his_attack_level       numeric(5)  not null,

    his_defence_xp         numeric(10) not null,
    his_defence_rank       numeric(10) not null,
    his_defence_level      numeric(5)  not null,

    his_strength_xp        numeric(10) not null,
    his_strength_rank      numeric(10) not null,
    his_strength_level     numeric(5)  not null,

    his_hitpoints_xp       numeric(10) not null,
    his_hitpoints_rank     numeric(10) not null,
    his_hitpoints_level    numeric(5)  not null,

    his_ranged_xp          numeric(10) not null,
    his_ranged_rank        numeric(10) not null,
    his_ranged_level       numeric(5)  not null,

    his_prayer_xp          numeric(10) not null,
    his_prayer_rank        numeric(10) not null,
    his_prayer_level       numeric(5)  not null,

    his_magic_xp           numeric(10) not null,
    his_magic_rank         numeric(10) not null,
    his_magic_level        numeric(5)  not null,

    his_cooking_xp         numeric(10) not null,
    his_cooking_rank       numeric(10) not null,
    his_cooking_level      numeric(5)  not null,

    his_woodcutting_xp     numeric(10) not null,
    his_woodcutting_rank   numeric(10) not null,
    his_woodcutting_level  numeric(5)  not null,

    his_fletching_xp       numeric(10) not null,
    his_fletching_rank     numeric(10) not null,
    his_fletching_level    numeric(5)  not null,

    his_fishing_xp         numeric(10) not null,
    his_fishing_rank       numeric(10) not null,
    his_fishing_level      numeric(5)  not null,

    his_firemaking_xp      numeric(10) not null,
    his_firemaking_rank    numeric(10) not null,
    his_firemaking_level   numeric(5)  not null,

    his_crafting_xp        numeric(10) not null,
    his_crafting_rank      numeric(10) not null,
    his_crafting_level     numeric(5)  not null,

    his_smithing_xp        numeric(10) not null,
    his_smithing_rank      numeric(10) not null,
    his_smithing_level     numeric(5)  not null,

    his_mining_xp          numeric(10) not null,
    his_mining_rank        numeric(10) not null,
    his_mining_level       numeric(5)  not null,

    his_herblore_xp        numeric(10) not null,
    his_herblore_rank      numeric(10) not null,
    his_herblore_level     numeric(5)  not null,

    his_agility_xp         numeric(10) not null,
    his_agility_rank       numeric(10) not null,
    his_agility_level      numeric(5)  not null,

    his_thieving_xp        numeric(10) not null,
    his_thieving_rank      numeric(10) not null,
    his_thieving_level     numeric(5)  not null,

    his_slayer_xp          numeric(10) not null,
    his_slayer_rank        numeric(10) not null,
    his_slayer_level       numeric(5)  not null,

    his_farming_xp         numeric(10) not null,
    his_farming_rank       numeric(10) not null,
    his_farming_level      numeric(5)  not null,

    his_runecraft_xp       numeric(10) not null,
    his_runecraft_rank     numeric(10) not null,
    his_runecraft_level    numeric(5)  not null,

    his_hunter_xp          numeric(10) not null,
    his_hunter_rank        numeric(10) not null,
    his_hunter_level       numeric(5)  not null,

    his_construction_xp    numeric(10) not null,
    his_construction_rank  numeric(10) not null,
    his_construction_level numeric(5)  not null,

    his_gotr_rank  numeric(10) not null,
    his_gotr_kc numeric(5)  not null,

    his_sire_rank  numeric(10) not null,
    his_sire_kc numeric(5)  not null,

    his_hydra_rank  numeric(10) not null,
    his_hyrdra_kc numeric(5)  not null,

    his_artio_rank  numeric(10) not null,
    his_artio_kc numeric(5)  not null,

    his_barrows_rank  numeric(10) not null,
    his_barrows_kc numeric(5)  not null,

    his_bryophyta_rank  numeric(10) not null,
    his_bryophyta_kc numeric(5)  not null,

    his_callisto_rank  numeric(10) not null,
    his_callisto_kc numeric(5)  not null,

    his_calvarion_rank  numeric(10) not null,
    his_calvarion_kc numeric(5)  not null,

    his_cerberus_rank  numeric(10) not null,
    his_cerberus_kc numeric(5)  not null,

    his_cox_rank  numeric(10) not null,
    his_cox_kc numeric(5)  not null,

    his_coxcm_rank  numeric(10) not null,
    his_coxcm_kc numeric(5)  not null,

    his_ce_rank  numeric(10) not null,
    his_ce_kc numeric(5)  not null,

    his_cf_rank  numeric(10) not null,
    his_cf_kc numeric(5)  not null,

    his_zilyana_rank  numeric(10) not null,
    his_zilyana_kc numeric(5)  not null,

    his_corp_rank  numeric(10) not null,
    his_corp_kc numeric(5)  not null,

    his_ca_rank  numeric(10) not null,
    his_ca_kc numeric(5)  not null,

    his_prime_rank  numeric(10) not null,
    his_prime_kc numeric(5)  not null,

    his_rex_rank  numeric(10) not null,
    his_rex_kc numeric(5)  not null,

    his_supreme_rank  numeric(10) not null,
    his_supreme_kc numeric(5)  not null,

    his_deranged_rank  numeric(10) not null,
    his_deranged_kc numeric(5)  not null,

    his_duke_rank  numeric(10) not null,
    his_duke_kc numeric(5)  not null,

    his_graadror_rank  numeric(10) not null,
    his_graardor_kc numeric(5)  not null,

    his_mole_rank  numeric(10) not null,
    his_mole_kc numeric(5)  not null,

    his_gg_rank  numeric(10) not null,
    his_gg_kc numeric(5)  not null,

    his_hespori_rank  numeric(10) not null,
    his_hespori_kc numeric(5)  not null,

    his_kq_rank  numeric(10) not null,
    his_kq_kc numeric(5)  not null,

    his_kbd_rank  numeric(10) not null,
    his_kbd_kc numeric(5)  not null,

    his_kraken_rank  numeric(10) not null,
    his_kraken_kc numeric(5)  not null,

    his_kreearra_rank  numeric(10) not null,
    his_kreearra_kc numeric(5)  not null,

    his_kril_rank  numeric(10) not null,
    his_kril_kc numeric(5)  not null,

    his_mimic_rank  numeric(10) not null,
    his_mimic_kc numeric(5)  not null,

    his_nex_rank  numeric(10) not null,
    his_nex_kc numeric(5)  not null,


    his_nightmare_rank  numeric(10) not null,
    his_nightmare_kc numeric(5)  not null,

    his_phosanis_rank  numeric(10) not null,
    his_phosanis_kc numeric(5)  not null,

    his_obor_rank  numeric(10) not null,
    his_obor_kc numeric(5)  not null,

    his_muspah_rank  numeric(10) not null,
    his_muspah_kc numeric(5)  not null,

    his_sarachnis_rank  numeric(10) not null,
    his_sarachnis_kc numeric(5)  not null,

    his_scorpia_rank  numeric(10) not null,
    his_scorpia_kc numeric(5)  not null,

    his_skotizo_rank  numeric(10) not null,
    his_skotizo_kc numeric(5)  not null,

    his_spindel_rank  numeric(10) not null,
    his_spindel_kc numeric(5)  not null,

    his_tempoross_rank  numeric(10) not null,
    his_tempoross_kc numeric(5)  not null,

    his_gauntlet_rank  numeric(10) not null,
    his_gauntlet_kc numeric(5)  not null,

    his_cg_rank  numeric(10) not null,
    his_cg_kc numeric(5)  not null,

    his_leviathan_rank  numeric(10) not null,
    his_leviathan_kc numeric(5)  not null,

    his_whisperer_rank  numeric(10) not null,
    his_whisperer_kc numeric(5)  not null,

    his_tob_rank  numeric(10) not null,
    his_tob_kc numeric(5)  not null,

    his_tobhm_rank  numeric(10) not null,
    his_tobhm_kc numeric(5)  not null,

    his_thermy_rank  numeric(10) not null,
    his_thermy_kc numeric(5)  not null,

    his_toa_rank  numeric(10) not null,
    his_toa_kc numeric(5)  not null,

    his_toaexpert_rank  numeric(10) not null,
    his_toaexpert_kc numeric(5)  not null,

    his_zuk_rank  numeric(10) not null,
    his_zuk_kc numeric(5)  not null,

    his_jad_rank  numeric(10) not null,
    his_jad_kc numeric(5)  not null,

    his_vardorvis_rank  numeric(10) not null,
    his_vardorvis_kc numeric(5)  not null,

    his_venenatis_rank  numeric(10) not null,
    his_venenatis_kc numeric(5)  not null,

    his_vetion_rank  numeric(10) not null,
    his_vetion_kc numeric(5)  not null,

    his_vorkath_rank  numeric(10) not null,
    his_vorkath_kc numeric(5)  not null,

    his_wintertodt_rank  numeric(10) not null,
    his_wintertodt_kc numeric(5)  not null,

    his_zalcano_rank  numeric(10) not null,
    his_zalcano_kc numeric(5)  not null,

    his_zulrah_rank  numeric(10) not null,
    his_zulrah_kc numeric(5)  not null,
    
);