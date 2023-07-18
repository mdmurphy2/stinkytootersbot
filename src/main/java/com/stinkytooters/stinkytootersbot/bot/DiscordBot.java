package com.stinkytooters.stinkytootersbot.bot;

import com.stinkytooters.stinkytootersbot.bot.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;

@Named
public class DiscordBot {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final JDA discordClient;

    @Inject
    public DiscordBot(@Value("${discord.token}") String token, MessageListener messageListener) {
        this.discordClient = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MESSAGES)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(messageListener)
                .build();
    }

    public void listen() {
        try {
            this.discordClient.awaitReady();
        } catch (Exception ex) {
            logger.error("An error occurred while listening.", ex);
        }
    }
}
