package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.menu.Keys;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Main;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Settings;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Map;

public class ElementAction {

    public ElementAction() {
        super();
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
                    switch (resource) {
                        case "main" -> {
                            Main main = new Main();
                            try {
                                main.start((Stage) additionalInfo.get("stage"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            {

                            }
                        }
                        case "settings" -> {
                            Settings settings = new Settings();
                            try {
                                settings.start((Stage) additionalInfo.get("stage"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            {

                            }
                        }
                        case "keys" -> {
                            Keys keys = new Keys();
                            try {
                                keys.start((Stage) additionalInfo.get("stage"));
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            {

                            }
                        }
                    }
                }
            }
        };

        currentButton.setOnMousePressed(leftClickHandler);
    }

    public void addActionButtonEntered(Button currentButton) {
        ColorAdjust setBrightness = new ColorAdjust();
        setBrightness.setBrightness(-0.7);

        currentButton.setOnMouseEntered(e -> currentButton.setEffect(setBrightness));
    }

    public void addActionButtonExited(Button currentButton) {
        ColorAdjust setBrightness = new ColorAdjust();
        setBrightness.setBrightness(0);

        currentButton.setOnMouseExited(e -> currentButton.setEffect(setBrightness));
    }
}
