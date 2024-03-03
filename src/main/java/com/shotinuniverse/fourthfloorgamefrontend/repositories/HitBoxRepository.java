package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.HitBoxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LabelEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HitBoxRepository {

    public static ArrayList<HitBoxEntity> getHitBoxes(String ownerClass, int ownerId) throws SQLException {
        String query = getQueryForHitBoxes(ownerClass, ownerId);

        ArrayList<Object> arrayList = SqlQuery.getObjects(query);
        return setHitBoxes(arrayList);
    }

    private static ArrayList<HitBoxEntity> setHitBoxes(ArrayList<Object> elements) {
        ArrayList<HitBoxEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            HitBoxEntity entity = new HitBoxEntity();
            entity.setId((int) map.get("_id"));
            entity.setOwnerClass((String) map.get("class"));
            entity.setOwnerId((int) map.get("object_id"));
            entity.setName((String) map.get("name"));
            entity.setRelativeX((int) map.get("relativeX"));
            entity.setRelativeY((int) map.get("relativeY"));
            entity.setRelativeWidth((int) map.get("relativeWidth"));
            entity.setRelativeHeight((int) map.get("relativeHeight"));

            arrayList.add(entity);
        }

        return arrayList;
    }

    private static String getQueryForHitBoxes(String ownerClass, int ownerId) {
        return String.format("""
                select
                    hit_boxes.*
                from
                    hit_boxes as hit_boxes
                where
                    hit_boxes.class = '%s' and hit_boxes.object_id = %d
                """, "com.shotinuniverse.fourthfloorgame." + ownerClass, ownerId);
    }
}
