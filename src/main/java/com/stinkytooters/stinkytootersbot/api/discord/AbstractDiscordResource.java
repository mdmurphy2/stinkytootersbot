package com.stinkytooters.stinkytootersbot.api.discord;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractDiscordResource {

    private final Map<String, Command> commands;

    public AbstractDiscordResource() {
        this.commands = new HashMap<>();
    }

    public void addCommand(String string, Command command) {
        commands.put(string, command);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
