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
    his_construction_level numeric(5)  not null
);