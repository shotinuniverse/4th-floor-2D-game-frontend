package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.ButtonEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ButtonRepository {

    public static ArrayList<ButtonEntity> getButtonsForMenu(int ownerId) throws SQLException {
        String query = getQueryForButtons(ownerId);

        ArrayList<Object> buttons = SqlQuery.getObjects(query);
        return setArrayButtons(buttons);
    }

    private static ArrayList<ButtonEntity> setArrayButtons(ArrayList<Object> elements) {
        ArrayList<ButtonEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            ButtonEntity button = new ButtonEntity();
            button.setId((int) map.get("_id"));
            button.setDbClass((String) map.get("_class"));
            button.setOwner((int) map.get("_owner"));
            button.setOrder((int) map.get("order"));
            button.setStyle((String) map.get("style"));
            button.setPointX((int) map.get("pointX"));
            button.setPointY((int) map.get("pointY"));
            button.setHeight((int) map.get("height"));
            button.setWidth((int) map.get("width"));
            button.setText((String) map.get("text"));
            button.setResource((String) map.get("resource"));
            button.setAction((String) map.get("action"));
            button.setImage((String) map.get("image"));

            arrayList.add(button);
        }

        return arrayList;
    }

    private static String getQueryForButtons(int ownerId) {
        return String.format("""
                select
                    buttons.*, synonyms.synonym as text, styles.style as style,
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
                    left outer join styles as styles
                        on buttons.style = styles._id
                where
                    buttons._owner = '%d'
                order by
                    buttons."order" asc
                """, SessionManager.language, ownerId);
    }
}
