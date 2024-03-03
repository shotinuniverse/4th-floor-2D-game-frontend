package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.entities.HitBoxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.Points;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.HitBoxRepository;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.LevelRepository;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Character implements EventHandler<KeyEvent> {

    private List<Rectangle> hitBoxes;
    public Character(List<Rectangle> rectangleList) {
        this.hitBoxes = rectangleList;
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> move(Side.LEFT);
            case RIGHT -> move(Side.RIGHT);
            case UP -> move(Side.TOP);
            case DOWN -> move(Side.BOTTOM);
        }
    }

    public void move(Side side) {
        for (Rectangle hitBox: hitBoxes) {
            double xPos;
            double yPos;
            switch (side) {
                case LEFT -> {
                    xPos = (hitBox.getX() - 1) % 600;
                    if (xPos < 0) {
                        xPos = 600;
                    }
                    hitBox.setX(xPos);
                }
                case RIGHT -> {
                    xPos = (hitBox.getX() + 1) % 600;
                    hitBox.setX(xPos);
                }
                case TOP -> {
                    yPos = (hitBox.getY() - 1) % 600;
                    hitBox.setY(yPos);
                }
                case BOTTOM -> {
                    yPos = (hitBox.getY() + 1) % 600;
                    hitBox.setY(yPos);
                }
            }
        }
    }

    public static List<Rectangle> getHitBoxesRectangles(int levelNumber) throws SQLException {
        List<Rectangle> rectangleList = new ArrayList<>();

        LevelEntity levelEntity;
        try {
            levelEntity = LevelRepository.getLevel(levelNumber);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

            Color color = null;
            switch (hitBox.getName()) {
                case "head" -> {
                    color = Color.RED;
                }
                case "body" -> {
                    color = Color.BLUE;
                }
                case "foots" -> {
                    color = Color.BLACK;
                }
                case "left hand", "right hand" -> {
                    color = Color.YELLOW;
                }
            }

            rectangle.setFill(color);
            rectangleList.add(rectangle);
        }

        return rectangleList;
    }
}
