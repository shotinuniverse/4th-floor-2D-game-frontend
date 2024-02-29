package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LabelEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabelRepository {

    private static final String databaseClass = "com.shotinuniverse.fouthfloorgame.label";

    public static ArrayList<LabelEntity> getLabelsForMenu(int ownerId) throws SQLException {
        String query = getQueryForLabels(ownerId);

        ArrayList<Object> labels = SqlQuery.getObjects(query);
        return setArrayLabels(labels);
    }

    private static ArrayList<LabelEntity> setArrayLabels(ArrayList<Object> elements) {
        ArrayList<LabelEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            LabelEntity entity = new LabelEntity();
            entity.setId((int) map.get("_id"));
            entity.setDbClass((String) map.get("_class"));
            entity.setOwner((int) map.get("_owner"));
            entity.setOrder((int) map.get("order"));
            entity.setStyle((String) map.get("style"));
            entity.setPointX((int) map.get("pointX"));
            entity.setPointY((int) map.get("pointY"));
            entity.setHeight((int) map.get("height"));
            entity.setWidth((int) map.get("width"));
            entity.setText((String) map.get("text"));

            arrayList.add(entity);
        }

        return arrayList;
    }

    private static String getQueryForLabels(int ownerId) {
        return SqlQuery.getQueryForEditableElements(databaseClass, ownerId);
    }
}
