package com.stinkytooters.stinkytootersbot.resource.user;

import com.stinkytooters.stinkytootersbot.api.discord.AbstractDiscordResource;
import com.stinkytooters.stinkytootersbot.display.user.UserDisplayService;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
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

    public List<MessageCreateData> addUser(String message) {
        return Arrays.asList(userDisplayService.addUser(message));
    }

    private List<MessageCreateData> updateUser(String message) {
        return userDisplayService.updateUser(message);
    }

    private List<MessageCreateData> inactivateUser(String message) {
        return Arrays.asList(userDisplayService.inactivateUser(message));
    }

    private void addCommands() {
        addCommand("!useradd", this::addUser);
        addCommand("!userupdate", this::updateUser);
        addCommand("!userremove", this::inactivateUser);
    }
}
