package com.stinkytooters.stinkytootersbot.clients;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreLiteData;
import com.stinkytooters.stinkytootersbot.rsapi.hiscores.OsrsHiscoreObjectMapper;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;

@Named
public class OsrsHiscoreLiteClient {

    private static final String HISCORE_LITE_ENDPOINT = "https://secure.runescape.com/m=hiscore_oldschool/index_lite.ws?player=%s";

    @Inject
    private OkHttpClient httpClient;

    @Inject
    private OsrsHiscoreObjectMapper osrsHiscoreObjectMapper;

    public OsrsHiscoreLiteData getHiscoresForUser(String user) throws IOException {
        String url = String.format(HISCORE_LITE_ENDPOINT, user);
        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();

        return osrsHiscoreObjectMapper.deserialize(response.body().byteStream());
    }

}
