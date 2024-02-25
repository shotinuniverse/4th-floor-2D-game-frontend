package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.HttpConnector;
import com.shotinuniverse.fourthfloorgamefrontend.common.SQLQuery;
import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public final class MenuBuilder {

    public static Map<String, Object> getStructureMenu(String type) throws SQLException {
        return SQLQuery.getMenuByResolutionAndType(type);
    }

    public static void paintMenu(Stage stage, Pane root, Map<String, Object> structureMenu) throws SQLException {
        addImageViewTitle(root);
        setStageProperties(stage, structureMenu);
        setGroupProperties(root, structureMenu);

        addButtonsIfNeed(stage, root, (int) structureMenu.get("_id"));
    }

    private static void addImageViewTitle(Pane root) {
        String pathToImage = SessionManager.pathImages.substring(1) + "menu-label-1920-1080.png";
        String imagePath = SessionManager.classLoader.getResource(pathToImage).toExternalForm();
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
            String pathToImage = SessionManager.pathImages.substring(1) + structureMenu.get("image");
            String image = SessionManager.classLoader.getResource(pathToImage).toExternalForm();
            root.setStyle("-fx-background-image: url('" + image + "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;");
        }
    }

    private static void addButtonsIfNeed(Stage stage, Pane root, int idMenu) throws SQLException {
        ArrayList<Object> buttons = SQLQuery.getButtonsByOwner(idMenu);
        for (Object buttonItem: buttons) {
            addButton(stage, root, buttonItem);
        }
    }

    private static void addButton(Stage stage, Pane root, Object buttonItem) {
        Button button = new Button();
        HashMap objectButtonItem = ((HashMap) buttonItem);
        button.setText(String.valueOf(objectButtonItem.get("text")));

        String pathToImage = SessionManager.pathImages + objectButtonItem.get("image");
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
