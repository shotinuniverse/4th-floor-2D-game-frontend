package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.AnimationEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AnimationRepository {

    public static ArrayList<AnimationEntity> getAnimationsForCharacter(int ownerId) throws SQLException {
        String query = getQueryForCharacter(ownerId);

        ArrayList<Object> animations = SqlQuery.getObjects(query);
        return setArrayAnimations(animations);
    }

    private static ArrayList<AnimationEntity> setArrayAnimations(ArrayList<Object> elements) {
        ArrayList<AnimationEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            AnimationEntity animation = new AnimationEntity();
            animation.setId((int) map.get("_id"));
            animation.setType((String) map.get("type"));
            animation.setFrameNumber((int) map.get("frame_number"));
            animation.setAction((String) map.get("action"));
            animation.setValue((int) map.get("value"));
            animation.setOwnerId((int) map.get("object_id"));
            animation.setOwnerClass((String) map.get("class"));
            animation.setInMove((int) map.get("in_move"));

            arrayList.add(animation);
        }

        return arrayList;
    }

    private static String getQueryForCharacter(int ownerId) {
        return String.format("""
                select
                    animations.*
                from
                    animations as animations
                    inner join hit_boxes as hit_boxes
                    on animations.object_id = hit_boxes._id
                where
                    hit_boxes.object_id = %d and animations.class = '%s'
                order by
                    animations.object_id asc, animations.frame_number asc
                """, ownerId, SessionManager.packageProject + ".hitbox");
    }
}
