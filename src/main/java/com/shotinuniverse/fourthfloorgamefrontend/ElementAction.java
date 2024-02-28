package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Keys;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Main;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Screen;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Settings;
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
                                saveDataInDatabaseFromForm(
                                        (ArrayList<Object>) additionalInfo.get("data"),
                                        (Pane) additionalInfo.get("group"),
                                        (String) additionalInfo.get("rootName"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
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
                        case "screen" -> {
                            Screen screen = new Screen();
                            try {
                                screen.start((Stage) additionalInfo.get("stage"));
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

    private void saveDataInDatabaseFromForm(ArrayList<Object> data, Pane group, String rootName) throws SQLException {
        ArrayList<Map<String, Object>> outputData = new ArrayList<>();

        ObservableList<Node> observableList = group.getChildren();
        for (Object rowData: data) {
            HashMap<String, Object> map = (HashMap) rowData;
            String stringId = String.valueOf(map.get("_id"));
            setDataFromElement(observableList, stringId, outputData, map);
        }

        if (outputData.size() > 0) {
            if (Objects.equals(rootName, "keys")) {
                updateKeys(outputData);
            } else {
                updateData(outputData);
            }
        }
    }

    private void setDataFromElement(ObservableList<Node> observableList,
                                    String stringId, ArrayList<Map<String, Object>> outputData,
                                    HashMap<String, Object> map) {
        for(int i = 0; i < observableList.size(); i++) {
            Object currentField = observableList.get(i);
            if (currentField instanceof TextField textField
                    && ((String) map.get("_class")).contains("textfield")
                    && Objects.equals(textField.getId(), stringId)) {
                String text = textField.getText();
                if (!text.isEmpty() && text != map.get("text")) {
                    fillOutputData(outputData, map, text);
                }
            } else if (currentField instanceof ComboBox<?> comboBox
                    && ((String) map.get("_class")).contains("combobox")
                    && Objects.equals(comboBox.getId(), stringId)) {
                String text = String.valueOf(comboBox.getValue());
                if (!text.isEmpty()) {
                    fillOutputData(outputData, map, text);
                }
            } else if (currentField instanceof Slider slider
                    && ((String) map.get("_class")).contains("slider")
                    && Objects.equals(slider.getId(), stringId)) {
                Double value = slider.getValue();
                fillOutputData(outputData, map, String.valueOf(Math.round(value)));
            }
        }
    }

    private void fillOutputData(ArrayList<Map<String, Object>> outputData, HashMap<String, Object> map, String value) {
        Map<String, Object> outputMap = new HashMap<>();
        outputMap.put("_id", map.get("static_id"));
        outputMap.put("tableName", map.get("table_data"));
        outputMap.put("columnName", map.get("column_data"));
        outputMap.put("value", value);

        outputData.add(outputMap);
    }

    private void updateKeys(ArrayList<Map<String, Object>> outputData) throws SQLException {
        for (Map<String, Object> map: outputData) {
            String query = String.format("UPDATE %s SET presentation = '%s', %s = '%s' WHERE _id = %d",
                    map.get("tableName"), map.get("value"),
                    map.get("columnName"), (int) ((String) map.get("value")).charAt(0),
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