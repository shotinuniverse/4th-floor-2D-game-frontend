package com.shotinuniverse.fourthfloorgamefrontend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpConnector {

    private String connectionString;

    public HttpConnector(Boolean protectedConnection) {
        super();
        this.connectionString = (protectedConnection) ? "https://" : "http://" + SessionParameters.serverName
                + ":" + String.valueOf(SessionParameters.serverPort);
    }

    public Map<String, Object> getObject(String resource, String method) throws IOException {
        URL url = new URL( this.connectionString + resource);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);

        int status = con.getResponseCode();
        if (checkStatusSuccessfully(status)) {
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(con.getInputStream())
            );

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> jsonMap = mapper.readValue(inputStream, Map.class);

            return jsonMap;
        }

        return null;
    }

    private Boolean checkStatusSuccessfully(int status) {
        if (status < 300) {
            return true;
        } else {
            return false;
        }
    }
}
