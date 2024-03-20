package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.getStructureMenu;
import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.paintMenu;

public class Pause extends Application {

    private static Game gameClass;

    public Pause(Game parentClass) {
        gameClass = parentClass;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        String menuType = "Pause";

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        paintMenu(primaryStage, root, structureMenu);

        SessionManager.scene.setRoot(root);
    }

}
