package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.*;

public class Settings extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        String menuType = "settings";

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        paintMenu(primaryStage, root, structureMenu);

        SessionManager.scene.setRoot(root);
    }

}
