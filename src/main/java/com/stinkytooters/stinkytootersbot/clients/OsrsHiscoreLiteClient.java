package com.stinkytooters.stinkytootersbot.clients;

import com.squareup.okhttp.Response;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.api.osrs.hiscores.OsrsHiscoreObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.invoke.MethodHandles;
import java.util.Objects;
import java.util.Optional;

@Named
public class OsrsHiscoreLiteClient extends BaseHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String HISCORE_LITE_ENDPOINT = "https://secure.runescape.com/m=hiscore_oldschool/index_lite.ws?player=%s";

    private OsrsHiscoreObjectMapper osrsHiscoreObjectMapper;

    @Inject
    public OsrsHiscoreLiteClient(OsrsHiscoreObjectMapper osrsHiscoreObjectMapper) {
        this.osrsHiscoreObjectMapper = Objects.requireNonNull(osrsHiscoreObjectMapper, "OsrsHiscoreObjectMapper is required.");
    }

    public Optional<OsrsHiscoreLiteData> getHiscoresForUser(String user) {
        try  {
            Response response = get(String.format(HISCORE_LITE_ENDPOINT, user));
            return Optional.ofNullable(osrsHiscoreObjectMapper.deserialize(response.body().byteStream()));
        } catch (Exception ex) {
            return Optional.empty();
        }
    }

}
