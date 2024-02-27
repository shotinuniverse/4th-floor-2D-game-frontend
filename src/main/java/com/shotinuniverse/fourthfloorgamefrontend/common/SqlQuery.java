package com.shotinuniverse.fourthfloorgamefrontend.common;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.common.SqlConnector.*;

public class SqlQuery {

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
                switch (resultSet.getMetaData().getColumnTypeName(i)) {
                    case "INTEGER" -> map.put(resultSet.getMetaData().getColumnName(i), resultSet.getInt(i));
                    case "TEXT" -> map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                }
            }

            values.add(map);
        }

        return values;
    }

    public static ArrayList<Object> getSystemParams() throws SQLException {
        String query = "select * from system_params";

        return getObjects(query);
    }

}
