package com.stinkytooters.stinkytootersbot.configuration;

import com.squareup.okhttp.OkHttpClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import org.postgresql.ds.PGSimpleDataSource;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class SpringConfiguration {

    @Value("${token}")
    private String token;

    @Value("${channel.id}")
    private String channelId;

    @Inject
    private ApplicationContext applicationContext;

    @Bean
    public OkHttpClient httpClient() {
        return new OkHttpClient();
    }

    @Bean
    public DataSource stinkyTootersDataSource(@Value("${database.ip}") String databaseIp) {
        PGSimpleDataSource source = new PGSimpleDataSource();
        source.setUrl(String.format("jdbc:postgresql://%s/p1pg", databaseIp));
        source.setUser("postgres");
        source.setPassword("***REMOVED***");
        return source;
    }

    @Bean
    public NamedParameterJdbcTemplate namedJdbcTemplate(DataSource stinkyTootersDataSource) throws SQLException {
        return new NamedParameterJdbcTemplate(stinkyTootersDataSource);
    }

    @Bean
    public GatewayDiscordClient discordClient() {
        return DiscordClientBuilder.create(token).build().login().block();
    }

    @Bean
    public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory() {
        return new AutowiringSpringBeanJobFactory(applicationContext);
    }

    @Bean
    public Scheduler stinkyTootersScheduler(AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory) throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.setJobFactory(autowiringSpringBeanJobFactory);
        return scheduler;
    }

}
