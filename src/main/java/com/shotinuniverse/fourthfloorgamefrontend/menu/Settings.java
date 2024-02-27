package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.*;

public class Settings extends Application {

    private Pane root;
    private String menuType = "settings";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root = new Pane();

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        if (structureMenu != null) {
            paintMenu(primaryStage, this.root, structureMenu);
        }

        SessionManager.scene.setRoot(this.root);
    }

}
