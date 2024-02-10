package com.stinkytooters.stinkytootersbot.display.graph;


import com.stinkytooters.stinkytootersbot.api.discord.InvalidCommandFormatException;
import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Boss;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.HiscoreV2;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Skill;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.HiscoreEntry;
import com.stinkytooters.stinkytootersbot.display.beans.HiscoreGraphDisplayBean;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.stinkytooters.stinkytootersbot.service.v2.hiscore.HiscoreV2Service;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Named
public class GraphDisplayService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ZoneId CENTRAL_ZONE_ID = ZoneId.of("America/Chicago");

    private static final int DEFAULT_DAYS_BACK = 7;
    private static final int DEFAULT_MINIMUM_XP_GAIN = 200_000;
    private static final Skill DEFAULT_SKILL = Skill.OVERALL;

    private static final String USAGE = "!graph [-d <daysBack>] [-m <minimumXpGained>] [-u <user> | -st (stinky tooters)] [-skill <skill>] [-boss <boss>] [-step]";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred while generating a graph, please try again.";
    private static final Set<String> ST_USERS = Stream.of("st jamie", "st juicy", "st snag", "st zecuity", "me rf")
            .collect(Collectors.toSet());

    private final HiscoreV2Service hiscoreService;
    private final UserService userService;

    @Inject
    public GraphDisplayService(HiscoreV2Service hiscoreService, UserService userService) {
        this.hiscoreService = Objects.requireNonNull(hiscoreService, "HiscoreService is required.");
        this.userService = Objects.requireNonNull(userService, "UserService is not required.");
    }

    public MessageCreateData createGraph(String command) {
        try {
            GenerateGraphRequest request = parseParameters(command);
            validateGenerateGraphRequest(request);
            return tryGenerateGraph(command, request);
        } catch (InvalidCommandFormatException ex) {
            String message = String.format("Given command (%s) was an invalid format. %s", command, ex.getMessage());
            logger.warn(message);
            return generateUsageMessage(ex.getMessage());
        } catch (Exception ex) {
            String message = String.format("An exception occurred while generating a graph for command (%s). %s", command, ex.getMessage());
            logger.error(message, ex);
            return generateUnexpectedErrorMessage();
        }

    }

    private MessageCreateData tryGenerateGraph(String command, GenerateGraphRequest request) {
        try {
            byte[] graph = generateGraph(request);
            if (graph != null && graph.length > 0) {
                return generateGraphMessage(graph);
            } else {
                logger.error("Graph for command ({}) was null or empty after generating.", command);
                return generateUnexpectedErrorMessage();
            }
        } catch (GraphGenerationException ex) {
            String message = String.format("An error occurred while generating a graph. (%s).", ex.getMessage());
            logger.warn(message);
            return generateUsageMessage(ex.getMessage());
        }
    }


    private GenerateGraphRequest parseParameters(String command) {
        List<String> commandParts = Arrays.stream(command.split(" ")).collect(Collectors.toList());
        int daysBack = DEFAULT_DAYS_BACK;
        if (commandParts.contains("-d")) {
            daysBack = getDaysBack(commandParts);
        }

        int minimumXpGain = DEFAULT_MINIMUM_XP_GAIN;
        if (commandParts.contains("-m")) {
            minimumXpGain = getMinimumXpGain(commandParts);
        }

        boolean stOnly = false;
        if (commandParts.contains("-st")) {
            stOnly = true;
        }

        String user = null;
        if (commandParts.contains("-u")) {
            user = getUser(commandParts);
        }

        Skill skill = null;
        if (commandParts.contains("-skill")) {
            skill = getSkill(commandParts);
        }

        Boss boss = null;
        if (commandParts.contains("-boss")) {
            boss = getBoss(commandParts);
        }

        GraphType graphType = GraphType.LINE;
        if (commandParts.contains("-step")) {
            graphType = GraphType.STEP;
        }

        return new GenerateGraphRequest.Builder()
                .withDaysBack(daysBack)
                .withMinimumXpGain(minimumXpGain)
                .withStOnly(stOnly)
                .withGraphType(graphType)
                .withFilterUser(user)
                .withSkil(skill)
                .withBoss(boss)
                .build();
    }

    private void validateGenerateGraphRequest(GenerateGraphRequest request) {
        if (request.getSkill() == null && request.getBoss() == null) {
            request.setSkill(DEFAULT_SKILL);
        }

        if (request.getSkill() != null && request.getBoss() != null) {
            throw new InvalidCommandFormatException("A skill and a boss cannot be specified in the same graph.");
        }

        if (request.getFilterUser() != null) {
            if (request.isStOnly()) {
                throw new InvalidCommandFormatException("A filter user and st only cannot both be specified.");
            }
        }
    }

    private MessageCreateData generateUsageMessage(String prefix) {
        return new MessageCreateBuilder()
                .setContent(prefix + "\n" + USAGE)
                .build();
    }

    private MessageCreateData generateUnexpectedErrorMessage() {
        return new MessageCreateBuilder()
                .setContent(UNEXPECTED_ERROR)
                .build();
    }

    private MessageCreateData generateGraphMessage(byte[] file) {
        return new MessageCreateBuilder()
                .setFiles(FileUpload.fromData(file, "graph.png"))
                .build();
    }

    private int getDaysBack(List<String> commandParts) {
        int daysSpecifier =  commandParts.indexOf("-d");
        if (daysSpecifier + 1 >= commandParts.size()) {
            throw new InvalidCommandFormatException("A day specifier must have a corresponding numeric amount of days.");
        }

        String daysBackCandidate = commandParts.get(daysSpecifier + 1).trim();
        try {
            return Integer.parseInt(daysBackCandidate);
        } catch (NumberFormatException ex) {
            throw new InvalidCommandFormatException("The value following the days (-d) specifier must be numeric.");
        }
    }

    private int getMinimumXpGain(List<String> commandParts) {
        int daysSpecifier =  commandParts.indexOf("-m");
        if (daysSpecifier + 1 >= commandParts.size()) {
            throw new InvalidCommandFormatException("A minimum xp gain specifier must have a corresponding numeric amount of experience gained.");
        }

        String daysBackCandidate = commandParts.get(daysSpecifier + 1).trim();
        try {
            return Integer.parseInt(daysBackCandidate);
        } catch (NumberFormatException ex) {
            throw new InvalidCommandFormatException("The value following the minimum xp gain (-m) specifier must be numeric.");
        }
    }

    private String getUser(List<String> commandParts) {
        int userSpecifier = commandParts.indexOf("-u");
        if (userSpecifier + 1 >= commandParts.size())  {
            throw new InvalidCommandFormatException("A user specifier must have a corresponding user name.");
        }

        String username = "";
        for (int i = userSpecifier + 1; i < commandParts.size(); i++) {
            String part = commandParts.get(i).trim();
            if (!part.startsWith("-")) {
                username += " " + part;
            } else {
                break;
            }
        }
        username = username.trim();

        return username;
    }

    private Skill getSkill(List<String> commandParts) {
        int skillSpecifier = commandParts.indexOf("-skill");
        if (skillSpecifier + 1 >= commandParts.size())  {
            throw new InvalidCommandFormatException("A skill specifier must have a corresponding skill.");
        }

        String skillCandidate = commandParts.get(skillSpecifier + 1).trim();
        try {
            return Skill.valueOf(skillCandidate.toUpperCase());
        } catch (Exception ex) {
            throw new InvalidCommandFormatException("The value following the skill specifier (-skill) must be a skill.");
        }
    }

    private Boss getBoss(List<String> commandParts) {
        int bossSpecifier = commandParts.indexOf("-boss");
        if (bossSpecifier + 1 >= commandParts.size())  {
            throw new InvalidCommandFormatException("A skill specifier must have a corresponding skill.");
        }

        String bossCandidate = commandParts.get(bossSpecifier + 1).trim();
        try {
            return Boss.valueOf(bossCandidate.toUpperCase());
        } catch (Exception ex) {
            throw new InvalidCommandFormatException("The value following the boss specifier (-boss) must be a boss.");
        }

    }

    private byte[] generateGraph(GenerateGraphRequest request) {
        List<User> users = userService.getAllUsers()
                .stream()
                .filter(u -> u.getStatus() == UserStatus.ACTIVE)
                .collect(Collectors.toList());

        if (request.getFilterUser() != null && !request.getFilterUser().isEmpty()) {
            users = filterUser(users, request.getFilterUser());
        }

        if (request.isStOnly()) {
            users = filterStOnly(users);
        }

        if (users == null || users.isEmpty()) {
            throw new GraphGenerationException("No users were found after filtering to specified users.");
        }

        List<HiscoreGraphDisplayBean> graphViewBeans = new ArrayList<>();
        for (User user : users) {
            HiscoreGraphDisplayBean graphViewBean = getHiscoreGraphViewBeanForUser(user.getId(), user.getName(), request, Timescale.DAYS);
            if (request.getBoss() != null) {
               graphViewBeans.add(graphViewBean);
            } else if (request.getSkill() != null && graphViewBean.getDelta() > request.getMinimumXpGain()){
                graphViewBeans.add(graphViewBean);
            }
        }

        LineChart<String, Long> lineChart = createLineChartFromGraphViewBeans(graphViewBeans, request);
        byte[] file = null;
        try {
            file = writeLineChartToMemory(lineChart).get();
        } catch (Exception ex) {
            String message = "An error occurred while writing line chart to disk. %s";
            message = String.format(message, ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message);
        }
        return file;
    }

    private Future<byte[]> writeLineChartToMemory(LineChart lineChart) {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        Platform.runLater(() -> {
                Scene scene = new Scene(lineChart, 1024, 720);
                lineChart.applyCss();

                WritableImage writableImage = lineChart.snapshot(new SnapshotParameters(), new WritableImage(1024, 720));

                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", baos);
                } catch (Exception ex) {
                    logger.error("An exception occurred while writing the file to disk.", ex);
                }
                logger.info("Finished writing to output stream. ({}).", baos.size());

                future.complete(baos.toByteArray());
        });
        return future;
    }

    private LineChart<String, Long> createLineChartFromGraphViewBeans(List<HiscoreGraphDisplayBean> hiscoreGraphDisplayBeans, GenerateGraphRequest request) {
        hiscoreGraphDisplayBeans = hiscoreGraphDisplayBeans
                .stream()
                .sorted(Comparator.comparing(HiscoreGraphDisplayBean::getId))
                .collect(Collectors.toList());

        SortedSet<String> allLabels = new TreeSet<>();
        for (HiscoreGraphDisplayBean viewBean : hiscoreGraphDisplayBeans) {
            allLabels.addAll(viewBean.getLabels());
        }

        // Hacky setup required by java fx.
        JFXPanel panel = new JFXPanel();
        ObservableList<String> labels = new ObservableListWrapper<>(new ArrayList<>(allLabels));
        CategoryAxis dateAxis = new CategoryAxis(labels);
        dateAxis.setLabel("Date");

        long min = hiscoreGraphDisplayBeans.stream()
                .mapToLong(HiscoreGraphDisplayBean::getMin)
                .min().orElse(0);

        long max = hiscoreGraphDisplayBeans.stream()
                .mapToLong(HiscoreGraphDisplayBean::getMax)
                .max().orElse(0);

        NumberAxis experienceOrKillcountAxis;
        if (request.getSkill() != null) {
            experienceOrKillcountAxis = new NumberAxis(Math.max(roundToNearestMillion(min - 1_000_000), 0), roundToNearestMillion(max) + 1_000_000, 1_000_000);
            experienceOrKillcountAxis.setLabel("Experience");
        } else {
            experienceOrKillcountAxis = new NumberAxis(Math.max(roundToNearestTen(min - 10), 0), roundToNearestTen(max) + 10, 10);
            experienceOrKillcountAxis.setLabel("Boss KC");
        }

        LineChart<String, Long> lineChart = new LineChart(dateAxis, experienceOrKillcountAxis);
        for (HiscoreGraphDisplayBean viewBean : hiscoreGraphDisplayBeans) {
            XYChart.Series<String, Long> series = new XYChart.Series<String, Long>();
            series.setName(viewBean.getId());
            for (int i = 0; i < viewBean.getData().size(); i++) {
                String label = viewBean.getLabels().get(i);
                Long xp = viewBean.getData().get(i);
                series.getData().add(new XYChart.Data<>(label, xp));
            }

            lineChart.getData().add(series);
        }

        for (XYChart.Series<String, Long> series : lineChart.getData()) {
            for (XYChart.Data<String, Long> data : series.getData())  {
                Node node = data.getNode();
                node.setScaleX(0.25);
                node.setScaleY(0.25);
            }
        }

        return lineChart;
    }

    static long roundToNearestMillion(long input) {
        long i = (long) Math.ceil(input);
        return ((i + 999_999) / 1_000_000) * 1_000_000;
    }

    static long roundToNearestTen(long input) {
        long i = (long) Math.ceil(input);
        return ((i + 9) / 10) * 10;
    }

    private HiscoreGraphDisplayBean getHiscoreGraphViewBeanForUser(Long userId, String username, GenerateGraphRequest request, Timescale timescale) {
        logger.info("Getting hiscore graph view bean for user ({}) - ({}).", userId, username);
        Instant cutoffTime = LocalDate.now().atStartOfDay(CENTRAL_ZONE_ID).minusDays(request.getDaysBack()).toInstant();
        List<HiscoreV2> hiscores = hiscoreService.getHiscoresForUsersDaysBack(userId, request.getDaysBack());
        hiscores = filterAndSortHiscores(hiscores, request, cutoffTime, timescale);
        List<String> labels = buildLabels(hiscores, timescale);
        List<Long> data = buildData(hiscores, request);
        if (request.getGraphType() == GraphType.STEP) {
            addStepwiseFunctionPoint(labels, data);
        }
        return buildViewBean(username, labels, data, cutoffTime, request);
    }

    private void addStepwiseFunctionPoint(List<String> labels, List<Long> data) {
        List<String> iterLabels = new ArrayList<>(labels);
        List<Long> iterData = new ArrayList<>(data);

        for (int i = 0; i < iterData.size() - 1; i++) {
            // Duplicate second label and duplicate first point.
            String secondLabel = iterLabels.get(i + 1);
            Long firstPoint = iterData.get(i);
            labels.add(i, secondLabel);
            data.add(i, firstPoint);
        }
    }

    private List<HiscoreV2> filterAndSortHiscores(List<HiscoreV2> hiscores, GenerateGraphRequest request, Instant furthestTime, Timescale timescale) {
        Map<String, HiscoreV2> greatestScoreForTimescale = new HashMap<>();
        for (HiscoreV2 hiscore : hiscores) {
            if (hiscore.getUpdateTime().isAfter(furthestTime)) {
                String dateKey = getDateKey(hiscore, timescale);

                long value = -1;
                if (request.getSkill() != null) {
                    value = hiscore.getSkills().get(request.getSkill()).getXp();
                } else {
                    value = hiscore.getBosses().get(request.getBoss()).getKillcount();
                }

                HiscoreV2 scoreForTimescaleEntry = greatestScoreForTimescale.get(dateKey);
                long valueInTimescale = Integer.MIN_VALUE;
                if (scoreForTimescaleEntry != null) {
                    if (request.getSkill() != null) {
                        valueInTimescale = scoreForTimescaleEntry.getSkills().get(request.getSkill()).getXp();
                    } else {
                        valueInTimescale = hiscore.getBosses().get(request.getBoss()).getKillcount();
                    }
                }

                if (value > valueInTimescale) {
                    greatestScoreForTimescale.put(dateKey, hiscore);
                }
            }
        }

        return greatestScoreForTimescale.values()
                .stream()
                .sorted(Comparator.comparing(HiscoreV2::getUpdateTime))
                .collect(Collectors.toList());
    }

    private String getDateKey(HiscoreV2 hiscore, Timescale timescale) {
        ZonedDateTime z = ZonedDateTime.ofInstant(hiscore.getUpdateTime(), CENTRAL_ZONE_ID);
        String dateKey = "";
        if (timescale == Timescale.DAYS) {
            dateKey = z.getMonthValue() + "-" + z.getDayOfMonth();
        } else if (timescale == Timescale.HOURS) {
            dateKey = z.getMonthValue() + "-" + z.getDayOfMonth() + "-"  + z.getHour();
        }
        return dateKey;
    }

    private List<String> buildLabels(List<HiscoreV2> hiscores, Timescale timescale) {
        Set<String> labels = new LinkedHashSet<>();
        for (HiscoreV2 hiscore : hiscores) {
            ZonedDateTime z = ZonedDateTime.ofInstant(hiscore.getUpdateTime(), CENTRAL_ZONE_ID);
            if (timescale == Timescale.DAYS) {
                labels.add(z.toLocalDate().toString());
            } else if (timescale == Timescale.HOURS){
                labels.add(z.toLocalDateTime().truncatedTo(ChronoUnit.HOURS).toString());
            }
        }
        return new ArrayList<>(labels);
    }

    private List<Long> buildData(List<HiscoreV2> hiscores, GenerateGraphRequest request) {
        if (request.getSkill() != null) {
            return hiscores.stream().map(hs -> hs.getSkills().get(request.getSkill()).getXp()).collect(Collectors.toList());
        } else {
            return hiscores.stream().map(hs -> hs.getBosses().get(request.getBoss()).getKillcount()).collect(Collectors.toList());
        }
    }

    private HiscoreGraphDisplayBean buildViewBean(String username, List<String> labels, List<Long> data, Instant furthestTime, GenerateGraphRequest request) {
        HiscoreGraphDisplayBean viewBean = new HiscoreGraphDisplayBean();
        Instant now = Instant.now();
        Period period = Period.between(LocalDate.ofInstant(furthestTime, CENTRAL_ZONE_ID), LocalDate.ofInstant(now, CENTRAL_ZONE_ID));
        String label = request.getSkill() != null ? request.getSkill().name().toLowerCase() : request.getBoss().name().toLowerCase();
        viewBean.setDataLabel(username + " - " + period.getMonths() + " month(s) and " + period.getDays() + " day(s) (" + label + ")");
        viewBean.setId(username);
        viewBean.setLabels(labels);
        viewBean.setData(data);
        return viewBean;
    }

    private List<User> filterStOnly(List<User> users) {
        List<User> stOnly = new ArrayList<>();
        for (User user : users) {
            for (String stUser : ST_USERS) {
                if (stUser.equalsIgnoreCase(user.getName())) {
                    stOnly.add(user);
                }
            }
        }
        return stOnly;
    }

    private List<User> filterUser(List<User> users, String user) {
        return users.stream().filter(u -> u.getName().equalsIgnoreCase(user)).collect(Collectors.toList());
    }

}
