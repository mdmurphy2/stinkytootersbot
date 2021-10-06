package com.stinkytooters.stinkytootersbot.clients;

import discord4j.discordjson.json.ComponentData;
import discord4j.discordjson.json.MessageCreateRequest;
import discord4j.rest.entity.RestChannel;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Named
public class StinkyTooterDiscordClient {

    @Inject
    private RestChannel stinkyTootersRestChannel;

    public void sendMessage(String message) {
        stinkyTootersRestChannel.createMessage(message).block(Duration.of(5000, ChronoUnit.MILLIS));
    }
}
