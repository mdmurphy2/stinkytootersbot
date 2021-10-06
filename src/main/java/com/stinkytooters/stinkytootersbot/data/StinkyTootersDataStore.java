package com.stinkytooters.stinkytootersbot.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreLiteData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

import javax.inject.Named;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Named
public class StinkyTootersDataStore {

    private final ObjectMapper mapper = new ObjectMapper();

   private File dataFile;
   private Map<String, PlayerScores> hiscores;

   public StinkyTootersDataStore(@Value("${data.file}") String dataFilePath) throws URISyntaxException, IOException {
       initializeDataStore(dataFilePath);
   }


   public void updateHiscore(String player, OsrsHiscoreLiteData hiscoreData) throws IOException {
       PlayerScores playerScores = new PlayerScores();
       playerScores.setPlayerName(player);
       playerScores.setScores(hiscoreData);

       hiscores.put(player, playerScores);
       saveToFile();
   }

   public PlayerScores getHiscore(String player) {
       return hiscores.get(player);
   }

   private void saveToFile() throws IOException {
       String saveString = mapper.writeValueAsString(hiscores.values());
       FileOutputStream outStream = new FileOutputStream(dataFile);
       PrintWriter out = new PrintWriter(outStream);
       out.write(saveString);
       out.close();
       outStream.close();
   }

    private void initializeDataStore(String dataFilePath) throws IOException, URISyntaxException {
        URL dataFileUrl = StinkyTootersDataStore.class.getResource(dataFilePath);
        if (dataFileUrl != null) {
            dataFile = new File(Objects.requireNonNull(StinkyTootersDataStore.class.getResource(dataFilePath)).toURI());
        } else {
            dataFile = new File(dataFilePath);
        }

        if (dataFile.exists()) {
            InputStream stream = new FileInputStream(dataFile);
            List<PlayerScores> scores = mapper.readValue(stream, new TypeReference<List<PlayerScores>>(){});
            hiscores = scores.stream().collect(Collectors.toMap(PlayerScores::getPlayerName, Function.identity()));
            stream.close();
        } else {
            hiscores = new HashMap<>();
            assert(dataFile.createNewFile());
        }
    }
}
