package com.stinkytooters.stinkytootersbot.resource.hiscore.graph;

import com.stinkytooters.stinkytootersbot.api.discord.AbstractDiscordResource;
import com.stinkytooters.stinkytootersbot.display.graph.GraphDisplayService;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Named
public class HiscoreGraphResource extends AbstractDiscordResource {

    private final GraphDisplayService graphDisplayService;

    @Inject
    public HiscoreGraphResource(GraphDisplayService graphDisplayService) {
        super();
        this.graphDisplayService = Objects.requireNonNull(graphDisplayService, "GraphDisplayService is required.");
        addCommands();
    }

    public List<MessageCreateData> graph(String message) {
        return Arrays.asList(graphDisplayService.createGraph(message));
    }

    private void addCommands() {
        addCommand("!graph", this::graph);
    }
}
