package com.shotinuniverse.fourthfloorgamefrontend;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

    public static void paintMenu(Stage stage, Pane root, Map<String, Object> structureMenu){
        addImageViewTitle(root);
        setStageProperties(stage, structureMenu);
        setGroupProperties(root, structureMenu);

        addButtonsIfNeed(stage, root, structureMenu);
    }

    private static void addImageViewTitle(Pane root) {
        String pathToImage = SessionParameters.pathImages.substring(1) + "menu-label-1920-1080.png";
        String imagePath = SessionParameters.classLoader.getResource(pathToImage).toExternalForm();
        Image image = new Image(imagePath, 863, 136, false, false);
        ImageView imageView = new ImageView(image);
        imageView.setX(150);
        imageView.setY(80);

        root.getChildren().add(imageView);
    }

    private static void setStageProperties(Stage stage, Map<String, Object> structureMenu) {
        if (structureMenu.containsKey("title")) {
            stage.setTitle((String) structureMenu.get("title"));
        }
    }

    private static void setGroupProperties(Pane root, Map<String, Object> structureMenu) {
        if (structureMenu.containsKey("image")) {
            String pathToImage = SessionParameters.pathImages.substring(1) + structureMenu.get("image");
            String image = SessionParameters.classLoader.getResource(pathToImage).toExternalForm();
            root.setStyle("-fx-background-image: url('" + image + "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;");
        }
    }

    private static void addButtonsIfNeed(Stage stage, Pane root, Map<String, Object> structureMenu) {
        if (!structureMenu.containsKey("buttons")) {
            return;
        }

        ArrayList<Object> buttons = (ArrayList<Object>) structureMenu.get("buttons");
        for (Object buttonItem: buttons) {
            addButton(stage, root, buttonItem);
        }
    }

    private static void addButton(Stage stage, Pane root, Object buttonItem) {
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
        buttonAction.addActionButtonEntered(button);
        buttonAction.addActionButtonExited(button);

        root.getChildren().add(button);
    }
}
