package com.stinkytooters.stinkytootersbot.api.discord;


import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.List;


public interface Command {
    List<MessageCreateData> execute(String message);
}
