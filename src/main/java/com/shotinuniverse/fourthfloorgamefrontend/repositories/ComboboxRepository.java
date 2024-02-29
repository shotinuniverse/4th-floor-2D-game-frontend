package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.ComboboxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LabelEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ComboboxRepository {

    private static final String databaseClass = "com.shotinuniverse.fourthfloorgame.combobox";

    public static ArrayList<ComboboxEntity> getComboBoxesForMenu(int ownerId) throws SQLException {
        String query = getQueryForComboBoxes(ownerId);

        ArrayList<Object> comboBoxes = SqlQuery.getObjects(query);
        return setArrayComboBoxes(comboBoxes);
    }

    private static ArrayList<ComboboxEntity> setArrayComboBoxes(ArrayList<Object> elements) throws SQLException {
        ArrayList<ComboboxEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            ComboboxEntity entity = new ComboboxEntity();
            entity.setId((int) map.get("_id"));
            entity.setDbClass((String) map.get("_class"));
            entity.setOwner((int) map.get("_owner"));
            entity.setOrder((int) map.get("order"));
            entity.setStyle((String) map.get("style"));
            entity.setPointX((int) map.get("pointX"));
            entity.setPointY((int) map.get("pointY"));
            entity.setHeight((int) map.get("height"));
            entity.setWidth((int) map.get("width"));
            entity.setValueTable((String) map.get("table_data"));
            entity.setValueColumn((String) map.get("column_data"));
            entity.setValueRowId((int) map.get("static_id"));

            setAvailableValues(entity);

            arrayList.add(entity);
        }

        return arrayList;
    }

    private static String getQueryForComboBoxes(int ownerId) {
        return SqlQuery.getQueryForEditableElements(databaseClass, ownerId);
    }

    private static void setAvailableValues(ComboboxEntity comboboxEntity) throws SQLException {
        String query = getQueryForAvailableValuesComboBox(comboboxEntity);

        ArrayList<Object> values = SqlQuery.getObjects(query);
        boolean firstRow = false;
        ObservableList<String> availableValues = FXCollections.observableArrayList();
        for (Object object: values) {
            HashMap<String, Object> map = (HashMap) object;
            if (!firstRow) {
                firstRow = true;
                comboboxEntity.setCurrentValue((String) map.get("text"));
            }

            availableValues.add((String) map.get("available"));
        }

        comboboxEntity.setAvailableValues(availableValues);
    }

    private static String getQueryForAvailableValuesComboBox(ComboboxEntity comboboxEntity) {
        return SqlQuery.getQueryForAvailableValuesComboBox(comboboxEntity);
    }
}
