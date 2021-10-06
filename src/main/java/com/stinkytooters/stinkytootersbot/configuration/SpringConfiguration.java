package com.stinkytooters.stinkytootersbot.configuration;

import com.squareup.okhttp.OkHttpClient;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreObjectMapper;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.rest.entity.RestChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {

    @Value("${token}")
    private String token;

    @Value("${channel.id}")
    private String channelId;

    @Bean
    public DiscordClient stinkyTootersDiscordClient() {
        return DiscordClientBuilder.create(token)
                .build();
    }

    @Bean
    public RestChannel stinkyTootersRestChannel(DiscordClient stinkyTootersDiscordClient) {
        return stinkyTootersDiscordClient.getChannelById(Snowflake.of(channelId));
    }

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient();
    }
}
