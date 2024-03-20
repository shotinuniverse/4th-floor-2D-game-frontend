package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

import static com.shotinuniverse.fourthfloorgamefrontend.menu.MenuBuilder.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws SQLException, ClassNotFoundException {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionParameters();

        String menuType = "Main";
        Pane root = new Pane();

        Map<String, Object> structureMenu = getStructureMenu(menuType, this);
        paintMenu(stage, root, structureMenu);

        if (SessionManager.scene == null) {
            sessionManager.setScene(root);
            stage.setScene(sessionManager.scene);
            stage.setFullScreen(false);
            stage.show();
        } else {
            SessionManager.scene.setRoot(root);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}