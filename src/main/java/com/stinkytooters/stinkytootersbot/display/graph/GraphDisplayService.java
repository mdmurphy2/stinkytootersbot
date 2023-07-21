package com.stinkytooters.stinkytootersbot.display.graph;


import com.stinkytooters.stinkytootersbot.api.discord.InvalidCommandFormatException;
import com.stinkytooters.stinkytootersbot.api.internal.exception.ServiceException;
import com.stinkytooters.stinkytootersbot.api.internal.hiscore.Hiscore;
import com.stinkytooters.stinkytootersbot.api.internal.user.User;
import com.stinkytooters.stinkytootersbot.api.internal.user.UserStatus;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.Skill;
import com.stinkytooters.stinkytootersbot.display.beans.HiscoreGraphDisplayBean;
import com.stinkytooters.stinkytootersbot.service.hiscore.HiscoreService;
import com.stinkytooters.stinkytootersbot.service.user.UserService;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.WritableImage;
import net.dv8tion.jda.api.entities.Message;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Named
public class GraphDisplayService {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final ZoneId CENTRAL_ZONE_ID = ZoneId.of("America/Chicago");

    private static final String USAGE = "!graph [-d <daysBack>] [-m <minimumXpGained>]";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred while generating a graph, please try again.";

    private final ExecutorService swingExecutor;
    private final HiscoreService hiscoreService;
    private final UserService userService;

    @Inject
    public GraphDisplayService(HiscoreService hiscoreService, UserService userService) {
        this.hiscoreService = Objects.requireNonNull(hiscoreService, "HiscoreService is required.");
        this.userService = Objects.requireNonNull(userService, "UserService is not required.");
        this.swingExecutor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable, "swing-thread"));
    }

    public MessageCreateData createGraph(String command) {
        try {
            List<String> commandParts = Arrays.stream(command.split(" ")).collect(Collectors.toList());

            int daysBack = 7;
            if (commandParts.contains("-d")) {
                daysBack = getDaysBack(commandParts);
            }

            int minimumXpGain = 200_000;
            if (commandParts.contains("-m")) {
                minimumXpGain = getMinimumXpGain(commandParts);
            }

            byte[] graph = generateGraph(daysBack, minimumXpGain);
            if (graph != null && graph.length > 0) {
                return generateGraphMessage(graph);
            } else {
                logger.error("Graph for command ({}) was null or empty after generating.", command);
                return generateUnexpectedErrorMessage();
            }

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
    private byte[] generateGraph(int daysBack, int mininmumXpGain) {
        List<User> users = userService.getAllUsers()
                .stream()
                .filter(user -> user.getStatus() == UserStatus.ACTIVE)
                .collect(Collectors.toList());

        List<HiscoreGraphDisplayBean> graphViewBeans = new ArrayList<>();
        for (User user : users) {
            HiscoreGraphDisplayBean graphViewBean = getHiscoreGraphViewBeanForUser(user.getId(), user.getName(), Skill.OVERALL, daysBack, Timescale.DAYS);
            if (graphViewBean.getDelta() > mininmumXpGain) {
                graphViewBeans.add(graphViewBean);
            }
        }

        LineChart<String, Integer> lineChart = createLineChartFromGraphViewBeans(graphViewBeans);
        Future<byte[]> fileFuture = swingExecutor.submit(() -> writeLineChartToMemory(lineChart));

        byte[] file = null;
        try {
            file = fileFuture.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            String message = "An error occurred while writing line chart to disk. %s";
            message = String.format(message, ex.getMessage());
            logger.error(message, ex);
            throw new ServiceException(message);
        }

        return file;
    }

    private byte[] writeLineChartToMemory(LineChart lineChart) throws InterruptedException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();

        Platform.runLater(() -> {
                Scene scene = new Scene(lineChart, 1024, 720);
                lineChart.applyCss();

                WritableImage writableImage = lineChart.snapshot(new SnapshotParameters(), new WritableImage(1024, 720));

                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", baos);
                } catch (Exception ex) {
                    logger.error("An exception occurred while writing the file to disk.", ex);
                }
        });
        Thread.sleep(250);

        int attempts = 0;
        while (baos.size() == 0 && attempts < 10) {
            Thread.sleep(250);
            attempts += 1;
        }

        return baos.toByteArray();
    }

    private LineChart<String, Integer> createLineChartFromGraphViewBeans(List<HiscoreGraphDisplayBean> hiscoreGraphDisplayBeans) {
        hiscoreGraphDisplayBeans = hiscoreGraphDisplayBeans.stream().sorted(Comparator.comparing(HiscoreGraphDisplayBean::getId)).collect(Collectors.toList());

        SortedSet<String> allLabels = new TreeSet<>();
        for (HiscoreGraphDisplayBean viewBean : hiscoreGraphDisplayBeans) {
            allLabels.addAll(viewBean.getLabels());
        }


        // Hacky setup required by java fx.
        JFXPanel panel = new JFXPanel();

        ObservableList<String> labels = new ObservableListWrapper<>(new ArrayList<>(allLabels));
        CategoryAxis dateAxis = new CategoryAxis(labels);
        dateAxis.setLabel("Date");

        int min = hiscoreGraphDisplayBeans.stream()
                .mapToInt(HiscoreGraphDisplayBean::getMin)
                .min().orElse(0);

        int max = hiscoreGraphDisplayBeans.stream()
                .mapToInt(HiscoreGraphDisplayBean::getMax)
                .max().orElse(0);

        NumberAxis xpAxis = new NumberAxis(Math.max(roundToNearestMillion(min - 1_000_000), 0), roundToNearestMillion(max) + 1_000_000, 1_000_000);
        xpAxis.setLabel("XP");

        LineChart<String, Integer> lineChart = new LineChart(dateAxis, xpAxis);
        for (HiscoreGraphDisplayBean viewBean : hiscoreGraphDisplayBeans) {
            XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
            series.setName(viewBean.getId());
            for (int i = 0; i < viewBean.getData().size(); i++) {
                String label = viewBean.getLabels().get(i);
                int xp = viewBean.getData().get(i);
                series.getData().add(new XYChart.Data<>(label, xp));
            }
            lineChart.getData().add(series);
        }
        return lineChart;
    }

    static long roundToNearestMillion(long input) {
        long i = (long) Math.ceil(input);
        return ((i + 999_999) / 1_000_000) * 1_000_000;
    }

    private HiscoreGraphDisplayBean getHiscoreGraphViewBeanForUser(Long userId, String username, Skill skill, int daysBack, Timescale timescale) {
        logger.info("Getting hiscore graph view bean for user ({}) - ({}).", userId, username);
        Instant cutoffTime = LocalDate.now().atStartOfDay(CENTRAL_ZONE_ID).minusDays(daysBack).toInstant();
        List<Hiscore> hiscores = hiscoreService.getHiscoresForUserDaysBack(userId, daysBack);

        hiscores = filterAndSortHiscores(hiscores, skill, cutoffTime, timescale);
        List<String> labels = buildLabels(hiscores, timescale);
        List<Integer> data = buildData(hiscores, skill);
        return buildViewBean(username, labels, data, cutoffTime, skill);
    }

    private List<Hiscore> filterAndSortHiscores(List<Hiscore> hiscores, Skill skill, Instant furthestTime, Timescale timescale) {
        Map<String, Hiscore> greatestScoreForTimescale = new HashMap<>();
        for (Hiscore hiscore : hiscores) {
            if (hiscore.getUpdateTime().isAfter(furthestTime)) {
                String dateKey = getDateKey(hiscore, timescale);
                int xpInEntry = hiscore.getXp(skill);
                Hiscore scoreForTimescaleEntry = greatestScoreForTimescale.get(dateKey);
                int xpInTimescale =  scoreForTimescaleEntry == null ? Integer.MIN_VALUE : scoreForTimescaleEntry.getXp(skill);
                if (xpInEntry > xpInTimescale) {
                    greatestScoreForTimescale.put(dateKey, hiscore);
                }
            }
        }

        return greatestScoreForTimescale.values()
                .stream()
                .sorted(Comparator.comparing(Hiscore::getUpdateTime))
                .collect(Collectors.toList());
    }

    private String getDateKey(Hiscore hiscore, Timescale timescale) {
        ZonedDateTime z = ZonedDateTime.ofInstant(hiscore.getUpdateTime(), CENTRAL_ZONE_ID);
        String dateKey = "";
        if (timescale == Timescale.DAYS) {
            dateKey = z.getMonthValue() + "-" + z.getDayOfMonth();
        } else if (timescale == Timescale.HOURS) {
            dateKey = z.getMonthValue() + "-" + z.getDayOfMonth() + "-"  + z.getHour();
        }
        return dateKey;
    }

    private List<String> buildLabels(List<Hiscore> hiscores, Timescale timescale) {
        Set<String> labels = new LinkedHashSet<>();
        for (Hiscore hiscore : hiscores) {
            ZonedDateTime z = ZonedDateTime.ofInstant(hiscore.getUpdateTime(), CENTRAL_ZONE_ID);
            if (timescale == Timescale.DAYS) {
                labels.add(z.toLocalDate().toString());
            } else if (timescale == Timescale.HOURS){
                labels.add(z.toLocalDateTime().truncatedTo(ChronoUnit.HOURS).toString());
            }
        }
        return new ArrayList<>(labels);
    }

    private List<Integer> buildData(List<Hiscore> hiscores, Skill skill) {
        return hiscores.stream().map(hs -> hs.getXp(skill)).collect(Collectors.toList());
    }

    private HiscoreGraphDisplayBean buildViewBean(String username, List<String> labels, List<Integer> data, Instant furthestTime, Skill skill) {
        HiscoreGraphDisplayBean viewBean = new HiscoreGraphDisplayBean();
        Instant now = Instant.now();
        Period period = Period.between(LocalDate.ofInstant(furthestTime, CENTRAL_ZONE_ID), LocalDate.ofInstant(now, CENTRAL_ZONE_ID));
        viewBean.setDataLabel(username + " - " + period.getMonths() + " month(s) and " + period.getDays() + " day(s) (" + skill.name().toLowerCase() + ")");
        viewBean.setId(username);
        viewBean.setLabels(labels);
        viewBean.setData(data);
        return viewBean;
    }

}
