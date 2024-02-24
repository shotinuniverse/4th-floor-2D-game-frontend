package com.shotinuniverse.fourthfloorgamefrontend;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpConnector {

    private String connectionString;
    private HttpURLConnection httpConnection;

    public HttpConnector(Boolean protectedConnection, String resource) throws IOException {
        super();
        this.connectionString = (protectedConnection) ? "https://" : "http://" + SessionParameters.serverName
                + ":" + String.valueOf(SessionParameters.serverPort);
        this.connectionString = this.connectionString + resource;

        this.httpConnection = getConnection();
    }

    public HttpConnector(Boolean protectedConnection, String resource, Map<String, String> params) throws IOException {
        super();
        this.connectionString = (protectedConnection) ? "https://" : "http://" + SessionParameters.serverName
                + ":" + String.valueOf(SessionParameters.serverPort);
        this.connectionString = this.connectionString + resource + getQuery(params);

        this.httpConnection = getConnection();
    }

    public Map<String, Object> getObject() throws IOException {
        httpConnection.setRequestMethod("GET");

        int status = httpConnection.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(httpConnection.getInputStream())
            );

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = objectMapper.readValue(inputStream, Map.class);

            return map;
        }

        return null;
    }

    private HttpURLConnection getConnection() throws IOException {
        URL url = new URL( this.connectionString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        return con;
    }

    private String getQuery(Map<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        result.append("?");
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
