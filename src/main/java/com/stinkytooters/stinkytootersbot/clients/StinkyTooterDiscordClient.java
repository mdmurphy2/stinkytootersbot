package com.stinkytooters.stinkytootersbot.clients;

import com.stinkytooters.stinkytootersbot.api.discord.Command;
import com.stinkytooters.stinkytootersbot.resource.user.UserResource;
import discord4j.common.util.Snowflake;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.*;

@Named
public class StinkyTooterDiscordClient {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Set<String> allowedChannelIds;
    private Map<String, Command> commands;
    private UserResource userResource;
    private GatewayDiscordClient discordClient;

    @Inject
    public StinkyTooterDiscordClient(UserResource userResource, GatewayDiscordClient discordClient, @Value("${allowed.channel}") String allowedChannel) {
        this.discordClient = Objects.requireNonNull(discordClient, "GatewayDiscordClient is required.");
        this.userResource = Objects.requireNonNull(userResource, "UserResource is required.");
        this.commands = new HashMap<>();
        this.allowedChannelIds = new HashSet<>();
        allowedChannelIds.add(allowedChannel);
        populateCommands();
    }

    private void populateCommands() {
        logger.info("Adding commands: {}", userResource.getCommands());
        commands.putAll(userResource.getCommands());
    }

    public void start() {
        discordClient.getEventDispatcher().on(MessageCreateEvent.class)
                .filter(event -> allowedChannelIds.contains(event.getMessage().getChannelId().asString()))
                .flatMap(event -> Mono.just(event.getMessage().getContent())
                .flatMap(content -> Flux.fromIterable(commands.entrySet())
                        .filter(entry -> content.startsWith(entry.getKey()))
                        .flatMap(entry ->
                            event.getMessage()
                                    .getChannel()
                                    .flatMap(channel -> channel.createMessage(entry.getValue().execute(content))).then()
                        ).then()))
                .subscribe();
        discordClient.onDisconnect().block();
    }

    public void sendMessage(String channelId, String message) {
        discordClient.getChannelById(Snowflake.of(channelId))
                .subscribe(channel -> channel.getRestChannel().createMessage(message).then().subscribe());
    }


}
