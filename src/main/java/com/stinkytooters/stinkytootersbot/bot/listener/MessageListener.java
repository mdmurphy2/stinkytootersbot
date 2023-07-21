package com.stinkytooters.stinkytootersbot.bot.listener;

import com.stinkytooters.stinkytootersbot.api.discord.AbstractDiscordResource;
import com.stinkytooters.stinkytootersbot.api.discord.Command;
import com.stinkytooters.stinkytootersbot.resource.user.UserResource;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Named
public class MessageListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Set<String> allowedChannelIds = new HashSet<>();
    private final List<AbstractDiscordResource> resources;

    @Inject
    public MessageListener(List<AbstractDiscordResource> resources, @Value("${allowed.channel}") String allowedChannel) {
        super();
        logger.info("Registering ({}) discord resources.", resources);
        this.resources = Objects.requireNonNull(resources, "Discord resources are required.");
        allowedChannelIds.add(allowedChannel);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        logger.info("Received message event ({}).", event);
        System.out.println("Received message event: " + event);

        String channelId = event.getMessage().getChannel().getId();
        if (!allowedChannelIds.contains(channelId)) {
            return;
        }

        final String message = event.getMessage().getContentDisplay();
        resources.stream()
                .map(AbstractDiscordResource::getCommands)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .filter(e -> message.startsWith(e.getKey()))
                .findFirst()
                .ifPresent(e -> executeCommand(e.getValue(), event, message));
    }

    private void executeCommand(Command command, MessageReceivedEvent event, String message) {
        CompletableFuture.supplyAsync(() -> command.execute(message))
                .thenAccept((result) -> sendMessage(event, result))
                .orTimeout(1, TimeUnit.MINUTES);
    }

    private void sendMessage(MessageReceivedEvent event, MessageCreateData message) {
        event.getChannel().asTextChannel().sendMessage(message).queue();
    }
}
