package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.*;

public class Main extends Application {

    private Pane root;
    private Scene scene;
    private String menuType = "main";

    @Override
    public void start(Stage stage) throws IOException, SQLException, ClassNotFoundException {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionParameters();

        this.root = new Pane();

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        if (structureMenu != null) {
            paintMenu(stage, this.root, structureMenu);
        }

        this.scene = new Scene(this.root,
                SessionManager.resolutionWidth, SessionManager.resolutionHeight);

        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}