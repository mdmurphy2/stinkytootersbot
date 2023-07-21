package com.stinkytooters.stinkytootersbot.resource.user;

import com.stinkytooters.stinkytootersbot.api.discord.AbstractDiscordResource;
import com.stinkytooters.stinkytootersbot.display.user.UserDisplayService;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;

@Named
public class UserResource extends AbstractDiscordResource {

    private final UserDisplayService userDisplayService;

    @Inject
    public UserResource(UserDisplayService userDisplayService) {
        super();
        this.userDisplayService = Objects.requireNonNull(userDisplayService, "UserDisplayService is required.");
        addCommands();
    }

    public MessageCreateData addUser(String message) {
        return userDisplayService.addUser(message);
    }

    private MessageCreateData updateUser(String message) {
        return userDisplayService.updateUser(message);
    }

    private MessageCreateData inactivateUser(String message) {
        return userDisplayService.inactivateUser(message);
    }

    private void addCommands() {
        addCommand("!useradd", this::addUser);
        addCommand("!userupdate", this::updateUser);
        addCommand("!userremove", this::inactivateUser);
    }
}
