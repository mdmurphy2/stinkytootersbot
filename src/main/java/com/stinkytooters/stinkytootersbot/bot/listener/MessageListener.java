package com.stinkytooters.stinkytootersbot.bot.listener;

import com.stinkytooters.stinkytootersbot.api.discord.Command;
import com.stinkytooters.stinkytootersbot.resource.user.UserResource;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Named
public class MessageListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Set<String> allowedChannelIds = new HashSet<>();
    private final UserResource userResource;

    @Inject
    public MessageListener(UserResource userResource, @Value("${allowed.channel}") String allowedChannel) {
        super();
        this.userResource = Objects.requireNonNull(userResource, "UserResource is required.");
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
        userResource.getCommands().entrySet()
                .stream()
                .filter(e -> message.startsWith(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .ifPresent(value -> executeCommand(value, event, message));
    }

    private void executeCommand(Command command, MessageReceivedEvent event, String message) {
        CompletableFuture.supplyAsync(() -> command.execute(message))
                .thenAccept((result) -> sendMessage(event, result))
                .orTimeout(1, TimeUnit.MINUTES);
    }

    private void sendMessage(MessageReceivedEvent event, String message) {
        event.getChannel().asTextChannel().sendMessage(message).queue();
    }
}
