package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.ComboboxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.TextFieldEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TextFieldRepository {

    private static final String databaseClass = "com.shotinuniverse.fourthfloorgame.textfield";

    public static ArrayList<TextFieldEntity> getTextFieldsForMenu(int ownerId) throws SQLException {
        String query = getQueryForTextFields(ownerId);

        ArrayList<Object> textFields = SqlQuery.getObjects(query);
        return setArrayTextFields(textFields);
    }

    private static ArrayList<TextFieldEntity> setArrayTextFields(ArrayList<Object> elements) throws SQLException {
        ArrayList<TextFieldEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            TextFieldEntity entity = new TextFieldEntity();
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
            entity.setEditable((int) map.get("editable"));

            boolean havePresentation = (int) map.get("havePresentation") == 1;
            entity.setHavePresentation(havePresentation);

            setCurrentValue(entity, havePresentation);

            arrayList.add(entity);
        }

        return arrayList;
    }

    private static String getQueryForTextFields(int ownerId) {
        return SqlQuery.getQueryForEditableElements(databaseClass, ownerId);
    }

    private static void setCurrentValue(TextFieldEntity textFieldEntity, boolean havePresentation) throws SQLException {
        String query = getQueryForCurrentValueTextField(textFieldEntity, havePresentation);

        Map<String, Object> value = SqlQuery.getObjectFromTable(query);
        if (value.containsKey("presentation")) {
            textFieldEntity.setPresentation((String) value.get("presentation"));
        }

        if (value.containsKey("text")) {
            textFieldEntity.setText((String) value.get("text"));
        }
    }

    private static String getQueryForCurrentValueTextField(TextFieldEntity textFieldEntity, boolean havePresentation) {
        return SqlQuery.getQueryForCurrentValueTextField(textFieldEntity, havePresentation);
    }
}
