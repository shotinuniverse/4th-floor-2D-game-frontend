package com.shotinuniverse.fourthfloorgamefrontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Main extends Application implements MenuBuilder {

    @Override
    public void start(Stage stage) throws IOException {
        setResolution();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),
                SessionParameters.resolutionWidth, SessionParameters.resolutionHeight);

        Map<String, Object> structureMenu = getStructureMenu();
        paintMenu(structureMenu);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void setResolution() {
        if (SessionParameters.resolutionHeight != 0) {
            return;
        }

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        SessionParameters.resolutionWidth = (int) screenBounds.getWidth();
        SessionParameters.resolutionHeight = (int) screenBounds.getHeight();
    }

    @Override
    public Map<String, Object> getStructureMenu() throws IOException {
        HttpConnector httpConnector = new HttpConnector(false);
        String resourceName = String.format("/menu/%dx%d",
                SessionParameters.resolutionWidth, SessionParameters.resolutionHeight);

        return httpConnector.getObject(resourceName, "GET");
    }

    @Override
    public void paintMenu(Map<String, Object> structureMenu) {

    }

//    private int getNumberLevel() throws IOException {
//
//        URL url = new URL("http://" + SessionParameters.serverName
//                + ":" + String.valueOf(SessionParameters.serverPort) + "/menu/1");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//
//        int status = con.getResponseCode();
//        if (status < 300) {
//            BufferedReader in = new BufferedReader(
//                    new InputStreamReader(con.getInputStream())
//            );
//
//            String inputLine;
//            StringBuffer content = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                content.append(inputLine);
//            }
//        }
//
//        return 0;
//    }
}