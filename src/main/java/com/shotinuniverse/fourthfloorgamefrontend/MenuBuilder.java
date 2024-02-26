package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public final class MenuBuilder {

    public static Map<String, Object> getStructureMenu(String type) throws SQLException {
        String query = getQueryForMenu(type);

        return SqlQuery.getObjectFromTable(query);
    }

    public static void paintMenu(Stage stage, Pane root, Map<String, Object> structureMenu) throws SQLException {
        addImageViewTitle(root);
        setStageProperties(stage, structureMenu);
        setGroupProperties(root, structureMenu);

        int owner = (int) structureMenu.get("_id");
        addButtonsIfNeed(stage, root, owner);
        addLabelsIfNeed(stage, root, owner);
        addTextFieldsIfNeed(stage, root, owner);
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
        String query = getQueryForButtons(idMenu);

        ArrayList<Object> buttons = SqlQuery.getObjects(query);
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

    private static void addLabelsIfNeed(Stage stage, Pane root, int idMenu) throws SQLException {
        String query = getQueryForLabels(idMenu);

        ArrayList<Object> labels = SqlQuery.getObjects(query);
        for (Object labelItem: labels) {
            addLabel(stage, root, labelItem);
        }
    }

    private static void addLabel(Stage stage, Pane root, Object labelItem) {
        Label label = new Label();
        HashMap objectButtonItem = ((HashMap) labelItem);
        label.setText(String.valueOf(objectButtonItem.get("text")));

        String style = (String) objectButtonItem.get("style");

        label.setStyle(style);

        if (objectButtonItem.containsKey("pointX")){
            int pointX = (int) objectButtonItem.get("pointX");
            label.setTranslateX((double) pointX);
        }

        if (objectButtonItem.containsKey("pointY")){
            int pointY = (int) objectButtonItem.get("pointY");
            label.setTranslateY((double) pointY);
        }

        int height = 0;
        int width = 0;
        if (objectButtonItem.containsKey("height")){
            height = (int) objectButtonItem.get("height");
            label.setMaxHeight((double) height);
        }

        if (objectButtonItem.containsKey("width")){
            width = (int) objectButtonItem.get("width");
            label.setMaxWidth((double) width);
        }

        label.setPrefSize(width, height);

        root.getChildren().add(label);
    }

    private static void addTextFieldsIfNeed(Stage stage, Pane root, int idMenu) throws SQLException {
        String query = getQueryForTextFields(idMenu);

        ArrayList<Object> textFields = SqlQuery.getObjects(query);
        for (Object textFieldItem: textFields) {
            addTextField(stage, root, textFieldItem);
        }
    }

    private static void addTextField(Stage stage, Pane root, Object textFieldItem) {
        TextField textField = new TextField();
        HashMap objectButtonItem = ((HashMap) textFieldItem);
        textField.setPromptText(String.valueOf(objectButtonItem.get("text")));

        String style = (String) objectButtonItem.get("style");

        textField.setStyle(style);

        if (objectButtonItem.containsKey("pointX")){
            int pointX = (int) objectButtonItem.get("pointX");
            textField.setTranslateX((double) pointX);
        }

        if (objectButtonItem.containsKey("pointY")){
            int pointY = (int) objectButtonItem.get("pointY");
            textField.setTranslateY((double) pointY);
        }

        int height = 0;
        int width = 0;
        if (objectButtonItem.containsKey("height")){
            height = (int) objectButtonItem.get("height");
            textField.setMaxHeight((double) height);
        }

        if (objectButtonItem.containsKey("width")){
            width = (int) objectButtonItem.get("width");
            textField.setMaxWidth((double) width);
        }

        textField.setPrefSize(width, height);

        root.getChildren().add(textField);
    }

    public static String getQueryForMenu(String type) {
        return String.format("""
                select
                    menu.*
                from
                    menu as menu
                left outer join menu_types as menu_types
                    on menu.type = menu_types._id
                where
                    menu.resolution = '%dx%d' and menu_types.name = '%s'
                """, SessionManager.resolutionWidth, SessionManager.resolutionHeight, type);
    }

    public static String getQueryForButtons(int ownerId) {
        return String.format("""
                select
                    buttons.*, synonyms.synonim as text,
                    points.pointX as pointX, points.pointY as pointY,
                    points.width as width, points.height as height
                from
                    buttons as buttons
                    left outer join synonyms as synonyms
                        on buttons._id = synonyms.object_id
                        and buttons._class = synonyms.class
                        and synonyms.language_code = '%s'
                    left outer join points as points
                        on buttons.points = points._id
                where
                    buttons._owner = '%d'
                order by
                    buttons."order" asc
                """, SessionManager.language, ownerId);
    }

    public static String getQueryForLabels(int ownerId) {
        return String.format("""
                select
                    labels.*, synonyms.synonim as text,
                    points.pointX as pointX, points.pointY as pointY,
                    points.width as width, points.height as height
                from
                    labels as labels
                    left outer join synonyms as synonyms
                        on labels._id = synonyms.object_id
                        and labels._class = synonyms.class
                        and synonyms.language_code = '%s'
                    left outer join points as points
                        on labels.points = points._id
                where
                    labels._owner = '%d'
                order by
                    labels."order" asc
                """, SessionManager.language, ownerId);
    }

    public static String getQueryForTextFields(int ownerId) {
        return String.format("""
                select
                   text_fields.*,
                   case
                       when keys_values.value is null
                           then keys_alt_values.alternative_presentation
                       else
                           keys_values.presentation
                   end as text,
                   points.pointX as pointX, points.pointY as pointY,
                   points.width as width, points.height as height
               from
                   text_fields as text_fields
                       left outer join points as points
                                       on text_fields.points = points._id
                       left outer join keys as keys_values
                                       on text_fields.static_id = keys_values._id
                                           and text_fields.column_data = 'value'
                       left outer join keys as keys_alt_values
                                       on text_fields.static_id = keys_alt_values._id
                                           and text_fields.column_data = 'alternative_value'
               where
                       text_fields._owner = '%d'
               order by
                   text_fields."order" asc
                """, ownerId);
    }
}
