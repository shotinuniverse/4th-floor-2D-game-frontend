package com.shotinuniverse.fourthfloorgamefrontend.menu;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.getStructureMenu;
import static com.shotinuniverse.fourthfloorgamefrontend.MenuBuilder.paintMenu;

public class Keys extends Application {

    private Pane root;
    private Scene scene;
    private String menuType = "keys";

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.root = new Pane();

        Map<String, Object> structureMenu = getStructureMenu(menuType);
        if (structureMenu != null) {
            paintMenu(primaryStage, root, structureMenu);
        }

        this.scene = new Scene(root,
                SessionManager.resolutionWidth, SessionManager.resolutionHeight);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

}
