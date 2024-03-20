package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.menu.MenuBuilder.*;

public class Settings extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        String menuType = "Settings";

        Map<String, Object> structureMenu = getStructureMenu(menuType, this);
        paintMenu(primaryStage, root, structureMenu);

        SessionManager.scene.setRoot(root);
    }

}
