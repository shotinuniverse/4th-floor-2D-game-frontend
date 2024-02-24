package com.shotinuniverse.fourthfloorgamefrontend;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public final class MenuBuilder {

    public static Map<String, Object> getStructureMenu(String type) throws IOException {
        String resourceName = "/menu";
        String resolution = String.format("%dx%d",
                SessionParameters.resolutionWidth, SessionParameters.resolutionHeight);

        Map<String, String> httpParams = new HashMap<String, String>() {{
            put("resolution", resolution);
            put("type", type);
        }};

        HttpConnector httpConnector = new HttpConnector(false, resourceName, httpParams);

        return httpConnector.getObject();
    }

    public static void paintMenu(Stage stage, Group root, Map<String, Object> structureMenu){
        if (!structureMenu.containsKey("buttons")) {
            return;
        }

        ArrayList<Object> buttons = (ArrayList<Object>) structureMenu.get("buttons");
        for (Object buttonItem: buttons) {
            Button button = new Button();
            LinkedHashMap objectButtonItem = ((LinkedHashMap) buttonItem);
            button.setText(String.valueOf(objectButtonItem.get("text")));

            String pathToImage = SessionParameters.pathLocalResources + objectButtonItem.get("image");
            String style = objectButtonItem.get("style") + "-fx-background-image: url('" + pathToImage + "')";

            button.setStyle(style);

            if (objectButtonItem.containsKey("pointX")){
                int pointX = (int) objectButtonItem.get("pointX");
                button.setTranslateX((double) pointX);
            }

            if (objectButtonItem.containsKey("pointY")){
                int pointY = (int) objectButtonItem.get("pointY");
                button.setTranslateY((double) pointY);
            }

            int height = 0;
            int width = 0;
            if (objectButtonItem.containsKey("height")){
                height = (int) objectButtonItem.get("height");
                button.setMaxHeight((double) height);
            }

            if (objectButtonItem.containsKey("width")){
                width = (int) objectButtonItem.get("width");
                button.setMaxWidth((double) width);
            }

            button.setPrefSize(width, height);

            ElementAction buttonAction = new ElementAction(stage);
            Map<String, Object> additionalInfo = new HashMap();
            additionalInfo.put("stage", stage);
            additionalInfo.put("group", root);

            String resource = (String) objectButtonItem.get("resource");
            String action = (String) objectButtonItem.get("action");

            buttonAction.addActionButtonOnClick(button, resource, action, additionalInfo);

            root.getChildren().add(button);
        }
    }
}
