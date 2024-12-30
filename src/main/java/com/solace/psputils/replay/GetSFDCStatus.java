package com.solace.psputils.replay;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GetSFDCStatus {
    private static Logger logger = LoggerFactory.getLogger(GetSFDCStatus.class);
    private static String url = "http://localhost:5010/sfdc/svc/response";

    public int getSfdcStatus(String payload) throws IOException, InterruptedException {
        logger.info("==================================================");
        logger.info("Invoking SFDC status check service.");
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Response: {} {}", response.statusCode(), response.body());
        return response.statusCode();
    }

}
