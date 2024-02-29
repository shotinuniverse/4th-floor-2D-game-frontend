package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public final class MenuBuilder {

    private static String rootName;
    private static ArrayList<Object> formData = new ArrayList<Object>();

    public static Map<String, Object> getStructureMenu(String type) throws SQLException {
        rootName = type;

        String query = getQueryForMenu(type);

        return SqlQuery.getObjectFromTable(query);
    }

    public static void paintMenu(Stage stage, Pane root, Map<String, Object> structureMenu) throws SQLException {
        addImageViewTitle(root);
        setStageProperties(stage, structureMenu);
        setGroupProperties(root, structureMenu);

        int owner = (int) structureMenu.get("_id");

        addLabelsIfNeed(stage, root, owner);
        addTextFieldsIfNeed(stage, root, owner);
        addComboBoxesIfNeed(stage, root, owner);
        addSlidersIfNeed(stage, root, owner);
        addButtonsIfNeed(stage, root, owner);
    }

    private static void addImageViewTitle(Pane root) {
        String pathToImage = SessionManager.getRelativePathToImage() + "menu-label-1920-1080.png";
        String imagePath = SessionManager.getPathToResource(pathToImage);

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
            String pathToImage = SessionManager.getRelativePathToImage() + structureMenu.get("image");
            String image = SessionManager.getPathToResource(pathToImage);
            root.setStyle("-fx-background-image: url('" + image + "'); " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: stretch;");

            //root.
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
        HashMap<String, Object> objectButtonItem = ((HashMap) buttonItem);
        button.setText(String.valueOf(objectButtonItem.get("text")));

        String pathToImage = SessionManager.pathToImages + objectButtonItem.get("image");
        String style = objectButtonItem.get("style")
                + "-fx-background-image: url('" + pathToImage + "');"
                + "-fx-background-position: center center; "
                + "-fx-background-repeat: stretch;";

        button.setStyle(style);

        setPoints(button, objectButtonItem);

        ElementAction buttonAction = new ElementAction();
        Map<String, Object> additionalInfo = new HashMap();
        additionalInfo.put("stage", stage);
        additionalInfo.put("group", root);
        additionalInfo.put("data", formData);
        additionalInfo.put("rootName", rootName);

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
        HashMap<String, Object> objectLabelItem = ((HashMap) labelItem);
        label.setText(String.valueOf(objectLabelItem.get("text")));

        String style = (String) objectLabelItem.get("style");
        label.setStyle(style);

        setPoints(label, objectLabelItem);

        root.getChildren().add(label);
    }

    private static void addTextFieldsIfNeed(Stage stage, Pane root, int idMenu) throws SQLException {
        String query = "";
        if (rootName == "keys") {
            query = getQueryForTextFieldsKeys(idMenu);
        } else return;

        ArrayList<Object> arrayList = SqlQuery.getObjects(query);
        for (Object textFieldItem: arrayList) {
            addTextField(stage, root, textFieldItem);

            formData.add(textFieldItem);
        }
    }

    private static void addTextField(Stage stage, Pane root, Object textFieldItem) {
        TextField textField = new TextField();
        HashMap<String, Object> objectTextFieldItem = ((HashMap) textFieldItem);
        textField.setPromptText(String.valueOf(objectTextFieldItem.get("text")));

        String style = (String) objectTextFieldItem.get("style");

        textField.setStyle(style);
        textField.setFocusTraversable(false);
        textField.setId(String.valueOf(objectTextFieldItem.get("_id")));

        if ((int) objectTextFieldItem.get("editable") == 0) {
            textField.setDisable(true);
            textField.setEditable(false);
        }

        setPoints(textField, objectTextFieldItem);

        root.getChildren().add(textField);
    }

    private static void addComboBoxesIfNeed(Stage stage, Pane root, int idMenu) throws SQLException {
        String query = "";
        if (rootName == "screen") {
            query = getQueryForComboBoxes(idMenu);
        } else return;

        ArrayList<Object> arrayList = SqlQuery.getObjects(query);
        for (Object textFieldItem: arrayList) {
            addComboBox(stage, root, textFieldItem);

            formData.add(textFieldItem);
        }
    }

    private static void addComboBox(Stage stage, Pane root, Object comboBoxItem) throws SQLException {
        ComboBox<String> comboBox = new ComboBox<>();
        HashMap<String, Object> objectComboBoxItem = ((HashMap) comboBoxItem);

        String query = String.format("""
            select
                value.text as text,
                av_values.value as available
            from
                (select
                    tb._id as _id,
                    tb._class as _class,
                    tb.%s as text
                from
                    %s as tb
                where
                    _id = %d) as value
                left outer join available_values as av_values
                on value._id = av_values.object_id
                    and value._class = av_values.class    
            """, objectComboBoxItem.get("column_data"),
                objectComboBoxItem.get("table_data"), (int) objectComboBoxItem.get("static_id"));

        ArrayList<Object> values = SqlQuery.getObjects(query);
        boolean firstRow = false;
        ObservableList<String> availableValues = FXCollections.observableArrayList();
        for (Object object: values) {
            HashMap<String, Object> map = (HashMap) object;
            if (!firstRow) {
                firstRow = true;
                comboBox.setValue((String) map.get("text"));
            }

            availableValues.add((String) map.get("available"));
        }

        comboBox.setItems(availableValues);

        String style = (String) objectComboBoxItem.get("style");
        comboBox.setStyle(style);
        comboBox.setFocusTraversable(false);
        comboBox.setId(String.valueOf(objectComboBoxItem.get("_id")));

        setPoints(comboBox, objectComboBoxItem);

        root.getChildren().add(comboBox);
    }

    private static void addSlidersIfNeed(Stage stage, Pane root, int idMenu) throws SQLException {
        String query = "";
        if (rootName == "screen") {
            query = getQueryForSliders(idMenu);
        } else return;

        ArrayList<Object> arrayList = SqlQuery.getObjects(query);
        for (Object sliderItem: arrayList) {
            addSlider(stage, root, sliderItem);

            formData.add(sliderItem);
        }
    }

    private static void addSlider(Stage stage, Pane root, Object sliderItem) throws SQLException {
        Slider slider = new Slider();
        HashMap<String, Object> objectSliderItem = ((HashMap) sliderItem);

        String style = (String) objectSliderItem.get("style");
        slider.setStyle(style);
        slider.setFocusTraversable(false);
        slider.setId(String.valueOf(objectSliderItem.get("_id")));

        setPoints(slider, objectSliderItem);

        String query = String.format("""
            select
                tb.%s as value
            from
                %s as tb
            where
                _id = %d    
            """, objectSliderItem.get("column_data"),
                objectSliderItem.get("table_data"), (int) objectSliderItem.get("static_id"));

        Map<String, Object> map = SqlQuery.getObjectFromTable(query);
        if (map.containsKey("value")) {
            slider.setValue(Integer.parseInt((String) map.get("value")));
        }

        slider.setMin(0);
        slider.setMax(100);

        slider.setMajorTickUnit(50);
        slider.setMinorTickCount(2);

        root.getChildren().add(slider);
    }

    private static void setPoints(Object elementObject, HashMap<String, Object> mapItem) {
        double pointX = getValuePoint(mapItem, "pointX");
        double pointY = getValuePoint(mapItem, "pointY");
        double height  = getValuePoint(mapItem, "height");
        double width  = getValuePoint(mapItem, "width");

        if(elementObject instanceof Button button) {
            button.setTranslateX(pointX);
            button.setTranslateY(pointY);
            button.setMaxHeight(height);
            button.setPrefHeight(height);
            button.setMaxWidth(width);
            button.setPrefWidth(width);
        } else if(elementObject instanceof Label label) {
            label.setTranslateX(pointX);
            label.setTranslateY(pointY);
            label.setMaxHeight(height);
            label.setPrefHeight(height);
            label.setMaxWidth(width);
            label.setPrefWidth(width);
        } else if(elementObject instanceof TextField textField) {
            textField.setTranslateX(pointX);
            textField.setTranslateY(pointY);
            textField.setMaxHeight(height);
            textField.setPrefHeight(height);
            textField.setMaxWidth(width);
            textField.setPrefWidth(width);
        } else if(elementObject instanceof ComboBox<?> comboBox) {
            comboBox.setTranslateX(pointX);
            comboBox.setTranslateY(pointY);
            comboBox.setMaxHeight(height);
            comboBox.setPrefHeight(height);
            comboBox.setMaxWidth(width);
            comboBox.setPrefWidth(width);
        } else if(elementObject instanceof Slider slider) {
            slider.setTranslateX(pointX);
            slider.setTranslateY(pointY);
            slider.setMaxHeight(height);
            slider.setPrefHeight(height);
            slider.setMaxWidth(width);
            slider.setPrefWidth(width);
        }
    }

    private static double getValuePoint(HashMap<String, Object> mapItem, String key) {
        double value = 0.0;
        if (mapItem.containsKey(key)){
            value = (int) mapItem.get(key);
        }

        return value;
    }

    //region sql query texts
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
                    buttons.*, synonyms.synonym as text,
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
                    labels.*, synonyms.synonym as text,
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

    public static String getQueryForTextFieldsKeys(int ownerId) {
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

    public static String getQueryForComboBoxes(int ownerId) {
        return String.format("""
                select
                   combo_boxes.*,
                   points.pointX as pointX, points.pointY as pointY,
                   points.width as width, points.height as height
               from
                   combo_boxes as combo_boxes
                       left outer join points as points
                       on combo_boxes.points = points._id
               where
                       combo_boxes._owner = '%d'
               order by
                   combo_boxes."order" asc
                """, ownerId);
    }

    public static String getQueryForSliders(int ownerId) {
        return String.format("""
                select
                   sliders.*,
                   points.pointX as pointX, points.pointY as pointY,
                   points.width as width, points.height as height
               from
                   sliders as sliders
                       left outer join points as points
                       on sliders.points = points._id
               where
                       sliders._owner = '%d'
               order by
                   sliders."order" asc
                """, ownerId);
    }

    public static String getQueryForComboBoxesValues() {

        return "";
    }
    //endregion
}
