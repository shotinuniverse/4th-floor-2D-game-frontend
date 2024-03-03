package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.entities.HitBoxEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.Points;
import com.shotinuniverse.fourthfloorgamefrontend.repositories.HitBoxRepository;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.*;

public class Character implements EventHandler<KeyEvent> {

    private List<Rectangle> hitBoxes;
    private int speedX;
    private int speedY;
    public int countFramesJump = 30;
    private int frameBeginJump = 0;
    private int frameEndJump = 0;
    private boolean onGround = false;
    final private Set<KeyCode> activeKeys = new HashSet<>();

    public Character(List<Rectangle> rectangleList) {
        this.hitBoxes = rectangleList;

        setPhysic();
    }

    private void setPhysic() {
        String query = String.format("""
                select
                    physic_properties.*
                from
                    physic_properties as physic_properties
                where
                    _id = 1
                """);

        try {
            Object object = SqlQuery.getObjectFromTable(query);
            Map<String, Object> map = (HashMap) object;
            speedX = (int) map.get("speedX");
            speedY = (int) map.get("speedY");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            activeKeys.add(event.getCode());
        } else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            activeKeys.remove(event.getCode());
        }
    }

    public Set<KeyCode> getActiveKeys() {
        return Collections.unmodifiableSet(activeKeys);
    }

    public void move() {
        Set<KeyCode> keyCodes = getActiveKeys();
        if (keyCodes.size() == 0)
            return;

        for (int i = 0; i < hitBoxes.size(); i++) {
            Rectangle hitBox = hitBoxes.get(i);
            double xPos;
            double yPos;
            if (keyCodes.contains(KeyCode.RIGHT) && keyCodes.contains(KeyCode.UP)) {
                if (!onGround && i == 0) {
                    break;
                }

                onGround = false;
                int currentFrame = Game.getCurrentFrame();
                if(frameBeginJump == 0) {
                    frameBeginJump = currentFrame;
                    frameEndJump = currentFrame + countFramesJump;
                }

                yPos = hitBox.getY() - currentFrame * speedY;
                xPos = hitBox.getX() + currentFrame * (speedX / 2);

                hitBox.setX(xPos);
                hitBox.setY(yPos);

                frameBeginJump += 1;

                if (frameBeginJump > frameEndJump) {
                    frameBeginJump = 0;
                    frameEndJump = 0;
                }
            }
            else if (keyCodes.contains(KeyCode.LEFT) && keyCodes.contains(KeyCode.UP)) {
                if (!onGround && i == 0) {
                    break;
                }

                onGround = false;
                int currentFrame = Game.getCurrentFrame();
                if (frameBeginJump == 0) {
                    frameBeginJump = currentFrame;
                    frameEndJump = currentFrame + countFramesJump;
                }

                yPos = hitBox.getY() - currentFrame * speedY;
                xPos = hitBox.getX() - currentFrame * (speedX / 2);

                hitBox.setX(xPos);
                hitBox.setY(yPos);

                frameBeginJump += 1;

                if (frameBeginJump > frameEndJump) {
                    frameBeginJump = 0;
                    frameEndJump = 0;
                }
            }
            else if (keyCodes.contains(KeyCode.RIGHT)) {
                    xPos = hitBox.getX() + speedX;
                    hitBox.setX(xPos);
                }
            else if (keyCodes.contains(KeyCode.LEFT)) {
                xPos = hitBox.getX() - speedX;
                hitBox.setX(xPos);
            }
            else if (keyCodes.contains(KeyCode.UP)) {
                if (!onGround && i == 0) {
                    break;
                }

                onGround = false;
                int currentFrame = Game.getCurrentFrame();
                if (frameBeginJump == 0) {
                    frameBeginJump = currentFrame;
                    frameEndJump = currentFrame + countFramesJump;
                }

                yPos = hitBox.getY() - currentFrame * speedY;


                hitBox.setY(yPos);

                frameBeginJump += 1;

                if (frameBeginJump > frameEndJump) {
                    frameBeginJump = 0;
                    frameEndJump = 0;
                }
            }
        }
    }

    public static List<Rectangle> getHitBoxesRectangles(LevelEntity levelEntity) throws SQLException {
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

    public void collisionHandler(ArrayList<LevelPlatform> platformArrayList) {
        checkCollisionsWithPlatforms(platformArrayList);
        setGravity();
    }

    public void setGravity() {
        if (onGround)
            return;

        double yPos;

        for (int i = 0; i < hitBoxes.size(); i++) {
            Rectangle hitBox = hitBoxes.get(i);
            yPos = hitBox.getY() + PhysicConst.characterGravity;
            hitBox.setY(yPos);
        }
    }

    public void checkCollisionsWithPlatforms(ArrayList<LevelPlatform> platformArrayList) {
        Rectangle foot = hitBoxes.get(0); // foot

        boolean collisionDetected = false;
        for (LevelPlatform levelPlatform: platformArrayList) {
            if (levelPlatform.getHitBox().getBoundsInParent().intersects(foot.getBoundsInParent()))
                collisionDetected = true;
        }

        onGround = collisionDetected;
    }
}
