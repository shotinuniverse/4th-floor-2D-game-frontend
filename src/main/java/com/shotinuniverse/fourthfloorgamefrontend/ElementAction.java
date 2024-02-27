package com.shotinuniverse.fourthfloorgamefrontend;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Keys;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Main;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Settings;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
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
                                saveDataInDatabaseFromTextFieldsForm(
                                        (ArrayList<Object>) additionalInfo.get("data"),
                                        (Pane) additionalInfo.get("group"));
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

    private void saveDataInDatabaseFromTextFieldsForm(ArrayList<Object> data, Pane group) throws SQLException {
        ArrayList<Map<String, Object>> outputData = new ArrayList<>();

        ObservableList<Node> observableList = group.getChildren();
        for (Object rowData: data) {
            HashMap<String, Object> map = (HashMap) rowData;
            String stringId = String.valueOf(map.get("_id"));
            for(int i = 0; i < observableList.size(); i++) {
                Object currentField = observableList.get(i);
                if (currentField instanceof TextField textField
                    && Objects.equals(textField.getId(), stringId)) {
                    String text = textField.getText();
                    if (!text.isEmpty() && text != map.get("text")) {
                        Map<String, Object> outputMap = new HashMap<>();
                        outputMap.put("_id", map.get("static_id"));
                        outputMap.put("tableName", map.get("table_data"));
                        outputMap.put("columnName", map.get("column_data"));
                        outputMap.put("value", text);

                        outputData.add(outputMap);
                    }
                }
            }
        }

        if (outputData.size() > 0) {
            updateKeys(outputData);
        }
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
}