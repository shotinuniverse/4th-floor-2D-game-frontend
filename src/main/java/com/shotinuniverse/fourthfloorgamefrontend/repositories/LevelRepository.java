package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.Points;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;

public class LevelRepository {

    public static LevelEntity getLevel(int numberLevel) throws SQLException {
        String query = getQueryForLevel(numberLevel);

        Object object = SqlQuery.getObjectFromTable(query);
        return setLevelProperties(object);
    }

    private static LevelEntity setLevelProperties(Object object) {
        Map<String, Object> map = (HashMap) object;
        LevelEntity level = new LevelEntity();
        level.setId((int) map.get("_id"));
        level.setDbClass((String) map.get("_class"));
        level.setName((String) map.get("name"));
        level.setNumber((int) map.get("number"));

        Points points = new Points();
        points.setPointX((int) map.get("pointX"));
        points.setPointY((int) map.get("pointY"));
        points.setWidth((int) map.get("width"));
        points.setHeight((int) map.get("height"));

        level.setCharPosition(points);

        return level;
    }

    private static String getQueryForLevel(int numberLevel) {
        return String.format("""
                select
                    levels.*,
                    points.pointX as pointX, points.pointY as pointY,
                    points.width as width, points.height as height
                from
                    levels as levels
                    left outer join points as points
                        on levels.char_position = points._id
                where
                    levels.number = '%d'
                """, numberLevel);
    }
}
