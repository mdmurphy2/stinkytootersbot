package com.stinkytooters.stinkytootersbot.clients;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BaseHttpClient {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int MAX_RETRIES = 5;
    private static final int INITIIAL_DELAY_MS = 100;
    private static final int EXPONENTIAL_BACKOFF_MULTIPLIER = 3;

    private OkHttpClient httpClient = new OkHttpClient();
    private ExecutorService httpExecutorService;

    @Value("${http.client.number.threads:10}")
    private int threads = 10;

    public BaseHttpClient() {
        httpExecutorService = Executors.newFixedThreadPool(threads);
    }


    public Response get(String url) throws Exception {
        Request request = new Request.Builder().url(url).build();
        Response response = null;

        Future<Response> responseFuture = httpExecutorService.submit(() -> makeRequest(request));
        try {
            response = responseFuture.get();
        } catch (Exception ex) {
            logger.error("An unexpected error occurred while performing a request.", ex);
            throw ex;
        }

        return response;
    }

    private Response makeRequest(Request request) {
        Response response = null;

        int retries = MAX_RETRIES;
        int currentDelay = INITIIAL_DELAY_MS;

        while(response == null && retries-- > 0) {
            try {
                response = httpClient.newCall(request).execute();
            } catch (Exception ex) {
                try {
                    Thread.sleep(currentDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                currentDelay *= EXPONENTIAL_BACKOFF_MULTIPLIER;
            }

        }

        return response;
    }
}
