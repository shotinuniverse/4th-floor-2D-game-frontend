package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.entities.*;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public final class MenuBuilder {

    private static String className;
    private static ArrayList<Object> formData = new ArrayList<Object>();

    public static Map<String, Object> getStructureMenu(String type) throws SQLException {
        className = type;

        String query = getQueryForMenu(type);

        return SqlQuery.getObjectFromTable(query);
    }

    public static void paintMenu(Stage stage, Pane root, Map<String, Object> structureMenu) throws SQLException {
        addImageViewTitle(root);
        setStageProperties(stage, structureMenu);
        setGroupProperties(root, structureMenu);

        int owner = (int) structureMenu.get("_id");

        addLabelsIfNeed(root, owner);
        addTextFieldsIfNeed(root, owner);
        addComboBoxesIfNeed(root, owner);
        addSlidersIfNeed(root, owner);
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
        ArrayList<ButtonEntity> buttons =
                ButtonRepository.getButtonsForMenu(idMenu);

        for (ButtonEntity buttonItem: buttons) {
            addButton(stage, root, buttonItem);
        }
    }

    private static void addButton(Stage stage, Pane root, ButtonEntity itemEntity) {
        Button button = new Button();

        button.setText(String.valueOf(itemEntity.getText()));

        String pathToImage = SessionManager.pathToImages + itemEntity.getImage();
        String style = itemEntity.getStyle()
                + "-fx-background-image: url('" + pathToImage + "');"
                + "-fx-background-position: center center; "
                + "-fx-background-repeat: stretch;";

        button.setStyle(style);

        int height = itemEntity.getHeight();
        int width = itemEntity.getWidth();
        button.setTranslateX(itemEntity.getPointX());
        button.setTranslateY(itemEntity.getPointY());
        button.setMaxHeight(height);
        button.setPrefHeight(height);
        button.setMaxWidth(width);
        button.setPrefWidth(width);

        ElementAction buttonAction = new ElementAction();
        Map<String, Object> additionalInfo = new HashMap();
        additionalInfo.put("stage", stage);
        additionalInfo.put("group", root);
        additionalInfo.put("data", formData);
        additionalInfo.put("className", className);

        String resource = itemEntity.getResource();
        String action = itemEntity.getAction();

        buttonAction.addActionButtonOnClick(button, resource, action, additionalInfo);
        buttonAction.addActionButtonEntered(button);
        buttonAction.addActionButtonExited(button);

        button.setId(String.valueOf(itemEntity.getId()));

        root.getChildren().add(button);
    }

    private static void addLabelsIfNeed(Pane root, int idMenu) throws SQLException {
        ArrayList<LabelEntity> labels =
                LabelRepository.getLabelsForMenu(idMenu);

        for (LabelEntity labelItem: labels) {
            addLabel(root, labelItem);
        }
    }

    private static void addLabel(Pane root, LabelEntity itemEntity) {
        Label label = new Label();
        label.setText(itemEntity.getText());

        label.setStyle(itemEntity.getStyle());

        int height = itemEntity.getHeight();
        int width = itemEntity.getWidth();
        label.setTranslateX(itemEntity.getPointX());
        label.setTranslateY(itemEntity.getPointY());
        label.setMaxHeight(height);
        label.setPrefHeight(height);
        label.setMaxWidth(width);
        label.setPrefWidth(width);

        label.setId(String.valueOf(itemEntity.getId()));

        root.getChildren().add(label);
    }

    private static void addTextFieldsIfNeed(Pane root, int idMenu) throws SQLException {
        ArrayList<TextFieldEntity> textFields =
                TextFieldRepository.getTextFieldsForMenu(idMenu);

        for (TextFieldEntity textFieldItem: textFields) {
            addTextField(root, textFieldItem);

            formData.add(textFieldItem);
        }
    }

    private static void addTextField(Pane root, TextFieldEntity itemEntity) {
        TextField textField = new TextField();
        if (itemEntity.isHavePresentation())
            textField.setPromptText(itemEntity.getPresentation());
        else
            textField.setPromptText(itemEntity.getText());

        textField.setStyle(itemEntity.getStyle());
        textField.setFocusTraversable(false);

        if (itemEntity.getEditable() == 0) {
            textField.setDisable(true);
            textField.setEditable(false);
        }

        int height = itemEntity.getHeight();
        int width = itemEntity.getWidth();
        textField.setTranslateX(itemEntity.getPointX());
        textField.setTranslateY(itemEntity.getPointY());
        textField.setMaxHeight(height);
        textField.setPrefHeight(height);
        textField.setMaxWidth(width);
        textField.setPrefWidth(width);

        textField.setId(String.valueOf(itemEntity.getId()));

        root.getChildren().add(textField);
    }

    private static void addComboBoxesIfNeed(Pane root, int idMenu) throws SQLException {
        ArrayList<ComboboxEntity> comboBoxes =
                ComboboxRepository.getComboBoxesForMenu(idMenu);

        for (ComboboxEntity comboboxItem: comboBoxes) {
            addComboBox(root, comboboxItem);

            formData.add(comboboxItem);
        }
    }

    private static void addComboBox( Pane root, ComboboxEntity itemEntity) {
        ComboBox<String> comboBox = new ComboBox<>();

        comboBox.setValue(itemEntity.getCurrentValue());
        comboBox.setItems(itemEntity.getAvailableValues());

        comboBox.setStyle(itemEntity.getStyle());
        comboBox.setFocusTraversable(false);
        comboBox.setId(String.valueOf(itemEntity.getId()));

        int height = itemEntity.getHeight();
        int width = itemEntity.getWidth();
        comboBox.setTranslateX(itemEntity.getPointX());
        comboBox.setTranslateY(itemEntity.getPointY());
        comboBox.setMaxHeight(height);
        comboBox.setPrefHeight(height);
        comboBox.setMaxWidth(width);
        comboBox.setPrefWidth(width);

        root.getChildren().add(comboBox);
    }

    private static void addSlidersIfNeed(Pane root, int idMenu) throws SQLException {
        ArrayList<SliderEntity> sliders =
                SliderRepository.getSliderForMenu(idMenu);

        for (SliderEntity sliderItem: sliders) {
            addSlider(root, sliderItem);

            formData.add(sliderItem);
        }
    }

    private static void addSlider(Pane root, SliderEntity itemEntity) {
        Slider slider = new Slider();

        slider.setStyle(itemEntity.getStyle());
        slider.setFocusTraversable(false);
        slider.setId(String.valueOf(itemEntity.getId()));

        int height = itemEntity.getHeight();
        int width = itemEntity.getWidth();
        slider.setTranslateX(itemEntity.getPointX());
        slider.setTranslateY(itemEntity.getPointY());
        slider.setMaxHeight(height);
        slider.setPrefHeight(height);
        slider.setMaxWidth(width);
        slider.setPrefWidth(width);

        slider.setValue(itemEntity.getCurrentValue());

        slider.setMin(itemEntity.getMin());
        slider.setMax(itemEntity.getMax());
        slider.setMajorTickUnit(itemEntity.getMajorTickUnit());
        slider.setMinorTickCount(itemEntity.getMinorTickCount());

        root.getChildren().add(slider);
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
    //endregion
}
