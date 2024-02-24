package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.*;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.*;

public class Main extends Application {

    private Group root;
    private Scene scene;
    private String menuType = "main";

    @Override
    public void start(Stage stage) throws IOException {
        setResolution();
        setCurrentProject();

        this.root = new Group();

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        if (structureMenu != null) {
            paintMenu(stage, root, structureMenu);
        }

        this.scene = new Scene(root,
                SessionParameters.resolutionWidth, SessionParameters.resolutionHeight);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public void setResolution() {
        if (SessionParameters.resolutionHeight != 0) {
            return;
        }

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        SessionParameters.resolutionWidth = (int) screenBounds.getWidth();
        SessionParameters.resolutionHeight = (int) screenBounds.getHeight();
    }

    public void setCurrentProject() {
        if (SessionParameters.classLoader != null) {
            return;
        }

        SessionParameters.classLoader = getClass().getClassLoader();
    }

}