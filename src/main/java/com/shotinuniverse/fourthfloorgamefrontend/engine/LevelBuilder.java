package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.entities.HitBoxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.PlatformEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.Points;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.HitBoxRepository;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.LevelRepository;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
        map.put("platforms", platformArrayList);
        map.put("character", character);

        return map;
    }

    private static ArrayList<LevelPlatform> createPlatforms(LevelEntity levelEntity, Pane root) {
        ArrayList<LevelPlatform> platformArrayList = new ArrayList<LevelPlatform>();

        List<Rectangle> hitBoxes = getPlatformsHitBoxesRectangles(levelEntity);
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
            hitBoxes = getCharacterHitBoxesRectangles(levelEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (Rectangle hitBox: hitBoxes)
            root.getChildren().add(hitBox);

        return new Character(hitBoxes);
    }

    public static List<Rectangle> getPlatformsHitBoxesRectangles(LevelEntity levelEntity) {

        List<Rectangle> rectangleList = new ArrayList<>();

        ArrayList<PlatformEntity> platformEntities = levelEntity.getPlatformEntities();
        for (PlatformEntity entity: platformEntities) {
            Rectangle rectangle = new Rectangle(entity.getPointX(),
                    entity.getPointY(), entity.getWidth(), entity.getHeight());

            rectangle.setFill(Color.DARKGREEN);

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    public static List<Rectangle> getCharacterHitBoxesRectangles(LevelEntity levelEntity) throws SQLException {
        List<Rectangle> rectangleList = new ArrayList<>();

        Points points = levelEntity.getCharPosition();
        int beginX = points.getPointX();
        int beginY = points.getPointY();

        ArrayList<HitBoxEntity> hitBoxEntities = HitBoxRepository.getHitBoxes("character", 1);

        for (HitBoxEntity hitBox: hitBoxEntities) {
            int relativeWidth = hitBox.getRelativeWidth();
            int relativeHeight = hitBox.getRelativeHeight();
            Rectangle rectangle = new Rectangle(
                    beginX + hitBox.getRelativeX(),
                    beginY + hitBox.getRelativeY(),
                    relativeWidth,
                    relativeHeight);

            String rectangleId = String.valueOf(hitBox.getId());
            rectangle.setId(rectangleId);

            //typesHitBoxes.put(hitBox.getType(), rectangleId);

            Color color = null;
            switch (hitBox.getType()) {
                case "head" -> {
                    color = Color.RED;
                }
                case "body" -> {
                    color = Color.BLUE;
                }
                case "foot" -> {
                    color = Color.BLACK;
                }
                case "hand" -> {
                    color = Color.YELLOW;
                }
            }

            rectangle.setFill(color);
            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

}
