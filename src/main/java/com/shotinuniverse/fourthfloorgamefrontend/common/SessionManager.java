package com.shotinuniverse.fourthfloorgamefrontend.common;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public final class SessionManager {

    public static String pathToDatabase = "src/main/resources/com/shotinuniverse/db.db";
    public static int resolutionWidth;
    public static int resolutionHeight;
    public static String serverName;
    public static int serverPort;
    public static ClassLoader classLoader;
    public static String pathToLocalResources;
    public static String pathToImages;
    public static String language;
    public static Scene scene;

    public void setSessionParameters() throws ClassNotFoundException, SQLException {
        SqlConnector.openConnection();

        setCurrentProject();

        ArrayList<Object> params = SqlQuery.getSystemParams();

        for (Object object: params) {
            Map<String, Object> map = (Map<String, Object>) object;
            if (map.get("name").equals("resolution")) {
               setResolution((String) map.get("value"));
            } else {
                setOtherParams((String) map.get("name"), (String) map.get("value"));
            }
        }
    }

    private void setResolution(String resolutionValue) {
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        if (resolutionValue == null) {
            SessionManager.resolutionWidth = (int) screenBounds.getWidth();
            SessionManager.resolutionHeight = (int) screenBounds.getHeight();
        } else {
            String[] arrayString = resolutionValue.split("x");
            SessionManager.resolutionWidth = Integer.parseInt(arrayString[0]);
            SessionManager.resolutionHeight = Integer.parseInt(arrayString[1]);
        }
    }

    private void setCurrentProject() {
        if (SessionManager.classLoader != null) {
            return;
        }

        SessionManager.classLoader = getClass().getClassLoader();
    }

    private void setOtherParams(String paramName, String paramValue){
        switch (paramName) {
            case "pathToLocalResources" -> pathToLocalResources = paramValue;
            case "pathToImages" -> pathToImages = paramValue;
            case "language" -> language = paramValue;
            case "serverName" -> serverName = paramValue;
            case "serverPort" -> serverPort = Integer.parseInt(paramValue);
        }
    }

    public void setScene(Pane root) {
        scene = new Scene(root,
                SessionManager.resolutionWidth, SessionManager.resolutionHeight);
    }

    public static String getRelativePathToImage() {
        return SessionManager.pathToImages.substring(1);
    }

    public static String getPathToResource(String pathToObject) {
        return Objects.requireNonNull(
                SessionManager.classLoader.getResource(pathToObject)).toExternalForm();
    }
}
