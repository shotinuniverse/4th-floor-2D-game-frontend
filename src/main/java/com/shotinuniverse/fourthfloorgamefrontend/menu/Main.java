package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.*;

public class Main extends Application {

    private Pane root;
    private String menuType = "main";

    @Override
    public void start(Stage stage) throws SQLException, ClassNotFoundException {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionParameters();

        this.root = new Pane();

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        if (structureMenu != null) {
            paintMenu(stage, this.root, structureMenu);
        }

        if (SessionManager.scene == null) {
            sessionManager.setScene(this.root);
            stage.setScene(sessionManager.scene);
            stage.setFullScreen(true);
            stage.show();
        } else {
            SessionManager.scene.setRoot(this.root);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}