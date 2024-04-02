package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.common.SessionManager;
import com.shotinuniverse.fourthfloorgamefrontend.entities.HitBoxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.PlatformEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.Points;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.HitBoxRepository;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.LevelRepository;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.*;

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
            if(entity.getVisible() == 0)
                continue;

            Rectangle rectangle = new Rectangle(entity.getPointX(),
                    entity.getPointY(), entity.getWidth(), entity.getHeight());

            String imageName = entity.getImage();
            if (imageName != null && !imageName.equals("")) {
                setTextureOnPlatform(rectangle, entity.getImage());
            } else {
                rectangle.setFill(Color.DARKGREEN);
            }

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
            int beginXRelative = beginX + hitBox.getRelativeX();
            int beginYRelative = beginY + hitBox.getRelativeY();
            Rectangle rectangle = new Rectangle(
                    beginXRelative,
                    beginYRelative,
                    relativeWidth,
                    relativeHeight);

            String rectangleId = String.valueOf(hitBox.getId());
            rectangle.setId(rectangleId);

            //typesHitBoxes.put(hitBox.getType(), rectangleId);

            String imageName = hitBox.getImage();
            if (imageName != null && !imageName.equals("")) {
                String pathToImage = SessionManager.getRelativePathToImage() + "textures/" + imageName;
                String imageString = SessionManager.getPathToResource(pathToImage);
                Image image = new Image(imageString);

                rectangle.setFill(new ImagePattern(image, beginXRelative, beginYRelative, relativeWidth, relativeHeight, false));
            }

            Color color = null;
            switch (hitBox.getType()) {
//                case "head" -> {
//                    color = Color.RED;
//                }
//                case "body" -> {
//                    color = Color.BLUE;
//                    rectangle.setFill(color);
//                }
                case "foot" -> {
                    color = Color.BLACK;
                    rectangle.setFill(color);
                }
                case "hand" -> {
                    color = Color.YELLOW;
                    rectangle.setFill(color);
                }
            }

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    public static void setTextureOnPlatform(Rectangle rectangle, String imageName) {
        String pathToImage = SessionManager.getRelativePathToImage() + "textures/" + imageName;
        String imageString = SessionManager.getPathToResource(pathToImage);
        Image image = new Image(imageString);
        double imageHeight = image.getHeight();
        double imageWidth = image.getWidth();
        double rectangleHeight = rectangle.getHeight();
        double rectangleWidth = rectangle.getWidth();
        double currentX = 0;
        double currentY = 0;

        while (currentX < rectangleWidth) {
            while (currentY < rectangleHeight) {
                rectangle.setFill(new ImagePattern(image, currentX, currentY, imageWidth, imageHeight, false));
                currentY += imageHeight;
            }
            currentX += imageWidth;
        }
    }

}
