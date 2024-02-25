package com.shotinuniverse.fourthfloorgamefrontend.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.common.SQLConnector.*;

public class SQLQuery {

    public static Map<String, Object> getObjectFromTable(String query) throws SQLException {
        resultSet = statement.executeQuery(query);

        Map<String, Object> map = new HashMap();
        while(resultSet.next()) {
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                switch (resultSet.getMetaData().getColumnTypeName(i)){
                    case "INTEGER": map.put(resultSet.getMetaData().getColumnName(i), resultSet.getInt(i)); break;
                    case "TEXT": map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i)); break;
                }

            }
        }

        return map;
    }

    public static ArrayList<Object> getObjects(String query) throws SQLException {
        resultSet = statement.executeQuery(query);

        ArrayList<Object> values = new ArrayList<Object>();
        while(resultSet.next()) {
            Map<String, Object> map = new HashMap();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                switch (resultSet.getMetaData().getColumnTypeName(i)){
                    case "INTEGER": map.put(resultSet.getMetaData().getColumnName(i), resultSet.getInt(i)); break;
                    case "TEXT": map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i)); break;
                }
            }

            values.add(map);
        }

        return values;
    }

    public static Map<String, Object> getMenuByResolutionAndType(String type) throws SQLException  {
        String query = String.format("""
                select
                    menu.*
                from
                    menu as menu
                left outer join menu_types as menu_types
                    on menu.type = menu_types._id
                where
                    menu.resolution = '%dx%d' and menu_types.name = '%s'
                """, SessionManager.resolutionWidth, SessionManager.resolutionHeight, type);
        return getObjectFromTable(query);
    }

    public static ArrayList<Object> getButtonsByOwner(int ownerId) throws SQLException  {
        String query = String.format("""
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
                    _owner = %d
                """, SessionManager.language, ownerId);

        return getObjects(query);
    }

}
