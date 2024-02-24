package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.menu.Main;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Settings;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Map;

public class ElementAction {

    private Stage stage;

    public ElementAction(Stage stage) {
        super();

        this.stage = stage;
    }

    public void addActionButtonOnClick(Button currentButton, String resource, String action, Map<String, Object> additionalInfo) {

        EventHandler<MouseEvent> leftClickHandler = event -> {
            if (MouseButton.PRIMARY.equals(event.getButton())){
                if (resource.isEmpty()){
                    if (action.equals("exit")) {
                        Platform.exit();
                        System.exit(0);
                    }
                } else {
                    if (resource.equals("/main")) {
                        Main main = new Main();
                        try {
                            main.start((Stage) additionalInfo.get("stage"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } {

                        }
                    } else if (resource.equals("/settings")) {
                        Settings settings = new Settings();
                        try {
                            settings.start((Stage) additionalInfo.get("stage"));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        } {

                        }
                    }
                }
            }
        };

        currentButton.setOnMousePressed(leftClickHandler);
    }
}
