package com.shotinuniverse.fourthfloorgamefrontend.common;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.sql.SQLException;

public final class SessionManager {

    public static int resolutionWidth;
    public static int resolutionHeight;
    public static String serverName = "srv-035-ts-05";
    public static int serverPort = 8080;
    public static ClassLoader classLoader;
    public static String pathLocalResources = "/com/shotinuniverse/";
    public static String pathImages= pathLocalResources + "images/";
    public static String dbPath = "src/main/resources/com/shotinuniverse/db.db";
    public static String language = "ru";

    public void setSessionParameters() throws ClassNotFoundException, SQLException {
        SqlConnector.openConnection();

        setResolution();
        setCurrentProject();
    }

    public void setResolution() {
        if (SessionManager.resolutionHeight != 0) {
            return;
        }

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        SessionManager.resolutionWidth = (int) screenBounds.getWidth();
        SessionManager.resolutionHeight = (int) screenBounds.getHeight();
    }

    public void setCurrentProject() {
        if (SessionManager.classLoader != null) {
            return;
        }

        SessionManager.classLoader = getClass().getClassLoader();
    }
}
