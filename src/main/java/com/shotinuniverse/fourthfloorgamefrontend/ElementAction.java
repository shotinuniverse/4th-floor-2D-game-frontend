package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.engine.Character;
import com.shotinuniverse.fourthfloorgamefrontend.engine.PhysicConst;
import com.shotinuniverse.fourthfloorgamefrontend.entities.ComboboxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.SliderEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.TextFieldEntity;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ElementAction {

    public ElementAction() {
        super();
    }

    public void addActionButtonOnClick(Button currentButton, String resource, String action, Map<String, Object> additionalInfo) {

        Stage stage = (Stage) additionalInfo.get("stage");
        Pane pane = (Pane) additionalInfo.get("group");
        String className = (String) additionalInfo.get("className");
        ArrayList<Object> addData = (ArrayList<Object>) additionalInfo.get("data");

        EventHandler<MouseEvent> leftClickHandler = event -> {
            if (MouseButton.PRIMARY.equals(event.getButton())){
                if (resource.isEmpty()){
                    switch (action) {
                        case "exit" -> {
                            Platform.exit();
                            System.exit(0);
                        }
                        case "save" -> {
                            try {
                                saveDataInDatabaseFromForm(addData, pane, className);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        case "start" -> {
                            try {
                                PhysicConst physicConst = new PhysicConst();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                            Game game = new Game(1);
                            try {
                                game.start(stage);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    try {
                        Class formClass = Class.forName("com.shotinuniverse.fourthfloorgamefrontend.menu." + resource);
                        Constructor constructor = formClass.getConstructor();
                        Object form = constructor.newInstance();
                        java.lang.reflect.Method method = formClass.getDeclaredMethod("start", Stage.class);
                        method.invoke(form, stage);
                    } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                             IllegalAccessException | InstantiationException e) {
                        throw new RuntimeException(e);
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

    private void saveDataInDatabaseFromForm(ArrayList<Object> data, Pane group, String className) throws SQLException {
        ArrayList<Map<String, Object>> outputData = new ArrayList<>();

        ObservableList<Node> observableList = group.getChildren();
        for (Object rowData: data) {
            setDataFromElement(observableList, outputData, rowData);
        }

        if (outputData.size() > 0) {
            if (Objects.equals(className, "Keys")) {
                updateKeys(outputData);
            } else {
                updateData(outputData);
            }
        }
    }

    private void setDataFromElement(ObservableList<Node> observableList,
                                    ArrayList<Map<String, Object>> outputData,
                                    Object rowData) {
        for(int i = 0; i < observableList.size(); i++) {
            Object currentField = observableList.get(i);
            if (currentField instanceof TextField textField
                    && rowData instanceof TextFieldEntity itemEntity
                    && Objects.equals(textField.getId(), String.valueOf(itemEntity.getId()))) {
                String text = textField.getText();
                if (!text.isEmpty() && text != itemEntity.getText()) {
                    fillOutputData(outputData, itemEntity.getValueRowId(),
                            itemEntity.getValueTable(), itemEntity.getValueColumn(), text);
                }
            } else if (currentField instanceof ComboBox<?> comboBox
                    && rowData instanceof ComboboxEntity itemEntity
                    && Objects.equals(comboBox.getId(), String.valueOf(itemEntity.getId()))) {
                String text = String.valueOf(comboBox.getValue());
                if (!text.isEmpty()) {
                    fillOutputData(outputData, itemEntity.getValueRowId(),
                            itemEntity.getValueTable(), itemEntity.getValueColumn(), text);
                }
            } else if (currentField instanceof Slider slider
                    && rowData instanceof SliderEntity itemEntity
                    && Objects.equals(slider.getId(), String.valueOf(itemEntity.getId()))) {
                Double value = slider.getValue();
                fillOutputData(outputData, itemEntity.getValueRowId(),
                        itemEntity.getValueTable(), itemEntity.getValueColumn(), String.valueOf(Math.round(value)));
            }
        }
    }

    private void fillOutputData(ArrayList<Map<String, Object>> outputData, int id,
                                String tableName, String columnName, String value) {
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("_id", id);
        outputMap.put("tableName", tableName);
        outputMap.put("columnName", columnName);
        outputMap.put("value", value);

        outputData.add(outputMap);
    }

    private void updateKeys(ArrayList<Map<String, Object>> outputData) throws SQLException {
        for (Map<String, Object> map: outputData) {
            String s = ((String) map.get("value")).toUpperCase();
            String query = String.format("UPDATE %s SET presentation = '%s, %s', %s = '%s' WHERE _id = %d",
                    map.get("tableName"), map.get("value"), s,
                    map.get("columnName"), s,
                    (int) map.get("_id"));

            SqlQuery.updateObject(query);
        }
    }

    private void updateData(ArrayList<Map<String, Object>> outputData) throws SQLException {
        for (Map<String, Object> map: outputData) {
            String query = String.format("UPDATE %s SET %s = '%s' WHERE _id = %d",
                    map.get("tableName"), map.get("columnName"),
                    map.get("value"), (int) map.get("_id"));

            SqlQuery.updateObject(query);
        }
    }
}