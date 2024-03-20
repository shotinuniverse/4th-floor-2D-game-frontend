package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.menu.MenuBuilder.getStructureMenu;
import static com.shotinuniverse.fourthfloorgamefrontend.menu.MenuBuilder.paintMenu;

public class Pause extends Application {

    private static Game gameClass;

    public Pause(Game parentClass) {
        gameClass = parentClass;
    }

    public static Game getGameClass() {
        return gameClass;
    }

    public static void setGameClass(Game gameClass) {
        Pause.gameClass = gameClass;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();
        String menuType = "Pause";

        Map<String, Object> structureMenu = getStructureMenu(menuType, this);
        paintMenu(primaryStage, root, structureMenu);

        SessionManager.scene.setRoot(root);
    }

}
