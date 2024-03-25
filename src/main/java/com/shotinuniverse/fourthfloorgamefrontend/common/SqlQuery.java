package com.shotinuniverse.fourthfloorgamefrontend.common;

import com.shotinuniverse.fourthfloorgamefrontend.entities.ComboboxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.SliderEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.TextFieldEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.common.SqlConnector.*;

public class SqlQuery {

    public static Map<String, Object> getObjectFromTable(String query) throws SQLException {
        resultSet = statement.executeQuery(query);

        Map<String, Object> map = new HashMap<>();
        while(resultSet.next())
            convertValueOnType(map);

        return map;
    }

    public static ArrayList<Object> getObjects(String query) throws SQLException {
        resultSet = statement.executeQuery(query);

        ArrayList<Object> values = new ArrayList<>();
        while(resultSet.next()) {
            Map<String, Object> map = new HashMap<>();
            convertValueOnType(map);
            values.add(map);
        }

        return values;
    }

    private static void convertValueOnType(Map<String, Object> map) throws SQLException {
        for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
            switch (resultSet.getMetaData().getColumnTypeName(i)) {
                case "INTEGER" -> map.put(resultSet.getMetaData().getColumnName(i), resultSet.getInt(i));
                case "TEXT" -> map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                case "REAL" -> map.put(resultSet.getMetaData().getColumnName(i), resultSet.getFloat(i));
            }
        }
    }

    public static void updateObject(String query) throws SQLException {
        statement.executeUpdate(query);
    }

    public static ArrayList<Object> getSystemParams() throws SQLException {
        String query = "select * from system_params";

        return getObjects(query);
    }

    public static String getQueryForEditableElements(String databaseClass, int ownerId) {
        return String.format("""
                select
                    editable_elements.*, synonyms.synonym as text, styles.style as style,
                    points.pointX as pointX, points.pointY as pointY,
                    points.width as width, points.height as height
                from
                    (select
                        *
                    from
                        editable_elements as editable_elements
                    where
                        _class = '%s') as editable_elements
                    left outer join synonyms as synonyms
                        on editable_elements._id = synonyms.object_id
                        and editable_elements._class = synonyms.class
                        and synonyms.language_code = '%s'
                    left outer join points as points
                        on editable_elements.points = points._id
                    left outer join styles as styles
                        on editable_elements.style = styles._id
                where
                    editable_elements._owner = '%d'
                order by
                    editable_elements."order" asc
                """, databaseClass, SessionManager.language, ownerId);
    }

    public static String getQueryForAvailableValuesComboBox(ComboboxEntity comboboxEntity) {
        return String.format("""
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
            """, comboboxEntity.getValueColumn(),
                comboboxEntity.getValueTable(),
                comboboxEntity.getValueRowId());
    }

    public static String getQueryForCurrentValueTextField(TextFieldEntity textFieldEntity, boolean havePresentation) {
        return String.format("""
            select
                tb._id as _id,
                tb._class as _class,
                tb.%s as text %s
            from
                %s as tb
            where
                _id = %d
            """, textFieldEntity.getValueColumn(),
                havePresentation ? ", tb.presentation as presentation" :  "" ,
                textFieldEntity.getValueTable(),
                textFieldEntity.getValueRowId());
    }

    public static String getQueryForCurrentValueSlider(SliderEntity sliderEntity) {
        return String.format("""
            select
                tb._id as _id,
                tb._class as _class,
                tb.%s as value
            from
                %s as tb
            where
                _id = %d
            """, sliderEntity.getValueColumn(),
                sliderEntity.getValueTable(),
                sliderEntity.getValueRowId());
    }
}
