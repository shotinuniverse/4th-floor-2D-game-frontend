package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.LevelRepository;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LevelBuilder {

    public static Map<String, Object> createLevel(int levelNumber, Pane root) {
        LevelEntity levelEntity;
        try {
            levelEntity = LevelRepository.getLevel(levelNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ArrayList<LevelPlatform> platformArrayList = createPlatforms(levelEntity, root);
        Character character = createChar(levelEntity, root);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("platfroms", platformArrayList);
        map.put("character", character);

        return map;
    }

    private static ArrayList<LevelPlatform> createPlatforms(LevelEntity levelEntity, Pane root) {
        ArrayList<LevelPlatform> platformArrayList = new ArrayList<LevelPlatform>();

        List<Rectangle> hitBoxes = LevelPlatform.getHitBoxesRectangles(levelEntity);
        for (Rectangle hitBox: hitBoxes){
            root.getChildren().add(hitBox);

            LevelPlatform levelPlatform = new LevelPlatform(hitBox);
            platformArrayList.add(levelPlatform);
        }

        return platformArrayList;
    }

    private static Character createChar(LevelEntity levelEntity, Pane root) {

        List<Rectangle> hitBoxes = null;
        try {
            hitBoxes = Character.getHitBoxesRectangles(levelEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Rectangle hitBox: hitBoxes)
            root.getChildren().add(hitBox);

        return new Character(hitBoxes);
    }

}
