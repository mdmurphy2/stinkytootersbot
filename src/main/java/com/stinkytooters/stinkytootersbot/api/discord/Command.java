package com.stinkytooters.stinkytootersbot.api.discord;


import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public interface Command {
    MessageCreateData execute(String message);
}
