package com.shotinuniverse.fourthfloorgamefrontend.repositories;

import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.SliderEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.TextFieldEntity;
import javafx.scene.control.Slider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SliderRepository {

    private static final String databaseClass = "com.shotinuniverse.fourthfloorgame.slider";

    public static ArrayList<SliderEntity> getSliderForMenu(int ownerId) throws SQLException {
        String query = getQueryForSliders(ownerId);

        ArrayList<Object> sliders = SqlQuery.getObjects(query);
        return setArraySliders(sliders);
    }

    private static ArrayList<SliderEntity> setArraySliders(ArrayList<Object> elements) throws SQLException {
        ArrayList<SliderEntity> arrayList = new ArrayList<>();

        for (Object object: elements) {
            Map<String, Object> map = (HashMap) object;
            SliderEntity entity = new SliderEntity(0, 100, 50, 2);
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

            setCurrentValue(entity);

            arrayList.add(entity);
        }

        return arrayList;
    }

    private static String getQueryForSliders(int ownerId) {
        return SqlQuery.getQueryForEditableElements(databaseClass, ownerId);
    }

    private static void setCurrentValue(SliderEntity sliderEntity) throws SQLException {
        String query = getQueryForCurrentValueSlider(sliderEntity);

        Map<String, Object> value = SqlQuery.getObjectFromTable(query);

        if (value.containsKey("value")) {
            sliderEntity.setCurrentValue(Integer.parseInt((String) value.get("value")));
        }
    }

    private static String getQueryForCurrentValueSlider(SliderEntity sliderEntity) {
        return SqlQuery.getQueryForCurrentValueSlider(sliderEntity);
    }
}
