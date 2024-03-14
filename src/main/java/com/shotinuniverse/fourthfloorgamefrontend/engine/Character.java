package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.*;

public class Character extends GameDynamicObject implements EventHandler<KeyEvent> {
    private final Set<KeyCode> KEY_UP = new HashSet<>();
    private final Set<KeyCode> KEY_RIGHT = new HashSet<>();
    private final Set<KeyCode> KEY_DOWN = new HashSet<>();
    private final Set<KeyCode> KEY_LEFT = new HashSet<>();
    public int limitFramesJump = 10;
    Side sideJump;
    private final Set<KeyCode> activeKeys = new HashSet<>();

    public Character(List<Rectangle> rectangleList) {
        super(rectangleList, 1);
        setControlKeys();
    }

    @Override
    public void handle(KeyEvent event) {
        if (KeyEvent.KEY_PRESSED.equals(event.getEventType())) {
            activeKeys.add(event.getCode());
        } else if (KeyEvent.KEY_RELEASED.equals(event.getEventType())) {
            activeKeys.remove(event.getCode());
        }
    }

    private void setControlKeys() {
        String query = "select action_types.name as name, keys.value as value from keys as keys inner join action_types as action_types on keys.action = action_types._id";
        try {
            ArrayList<Object> arrayList = SqlQuery.getObjects(query);
            for (Object object: arrayList) {
                Map<String, Object> map = (HashMap) object;
                if (map.get("name").equals("move forward"))
                    KEY_RIGHT.add(KeyCode.valueOf((String) map.get("value")));
                else if (map.get("name").equals("move back"))
                    KEY_LEFT.add(KeyCode.valueOf((String) map.get("value")));
                else if (map.get("name").equals("jump"))
                    KEY_UP.add(KeyCode.valueOf((String) map.get("value")));
                else if (map.get("name").equals("sneak"))
                    KEY_DOWN.add(KeyCode.valueOf((String) map.get("value")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void move() {
        if (!onGround){
            if (numberFrameEndJump != 0) {
                endJump();
            } else {
                return;
            }
        }

        Set<KeyCode> keyCodes = getActiveKeys();
        if (keyCodes.size() == 0)
            return;

        int countHitBoxes = hitBoxes.size() - 1;
        for (int i = 0; i <= countHitBoxes; i++) {
            Rectangle hitBox = hitBoxes.get(i);
            double xPos;
            if (haveMoveRight(keyCodes) && haveMoveUp(keyCodes)) {
                sideJump = Side.RIGHT;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (haveMoveLeft(keyCodes) && haveMoveUp(keyCodes)) {
                sideJump = Side.LEFT;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (haveMoveUp(keyCodes)) {
                sideJump = Side.TOP;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (haveMoveRight(keyCodes)) {
                xPos = hitBox.getX() + speedX;
                hitBox.setX(xPos);
            }
            else if (haveMoveLeft(keyCodes)) {
                xPos = hitBox.getX() - speedX;
                hitBox.setX(xPos);
            }
        }
    }

    private boolean haveMoveRight(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyRight: KEY_RIGHT) {
            if (keyCodes.contains(keyRight))
                haveMove = true;
        }
        return haveMove;
    }

    private boolean haveMoveLeft(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyLeft: KEY_LEFT) {
            if (keyCodes.contains(keyLeft))
                haveMove = true;
        }
        return haveMove;
    }

    private boolean haveMoveUp(Set<KeyCode> keyCodes) {
        boolean haveMove = false;
        for (KeyCode keyUp: KEY_UP) {
            if (keyCodes.contains(keyUp))
                haveMove = true;
        }
        return haveMove;
    }

    public Set<KeyCode> getActiveKeys() {
        return Collections.unmodifiableSet(activeKeys);
    }

    private void beginJump(Rectangle hitBox) {
        if (!onGround) {
            return;
        }

        int currentFrame = Game.getCurrentFrame();

        editCoordinatesInJump(hitBox);

        if(numberFrameEndJump == 0){
            numberFrameEndJump = currentFrame + limitFramesJump;
        }
    }

    private void endJump() {
        int currentFrame = Game.getCurrentFrame();

        int valueForComparison;
        if (Game.framesPerSecond - limitFramesJump <= currentFrame && currentFrame <= Game.framesPerSecond && numberFrameEndJump > 60) {
            valueForComparison = Game.framesPerSecond;
        } else {
            valueForComparison = numberFrameEndJump > Game.framesPerSecond ? numberFrameEndJump - Game.framesPerSecond : numberFrameEndJump;
        }

        if (currentFrame > valueForComparison) {
            numberFrameEndJump = 0;
            return;
        }

        for (Rectangle hitBox: hitBoxes) {
            editCoordinatesInJump(hitBox);
        }
    }

    private void editCoordinatesInJump(Rectangle hitBox) {
        double yPos = hitBox.getY() - speedY;
        if(yPos < 0 || yPos > 1080) {
            hitBox.setY(540);
            numberFrameEndJump = 0;
            return;
        } else {
            hitBox.setY(yPos);
        }

        double xPos = 0;
        double moveX = speedX * 1.7;
        switch (sideJump) {
            case RIGHT -> xPos = hitBox.getX() + moveX;
            case LEFT -> xPos = hitBox.getX() - moveX;
            case TOP -> xPos = hitBox.getX();
        }

        if (xPos < 0 || xPos > 1920) {
            hitBox.setX(40);
            numberFrameEndJump = 0;
            return;
        } else {
            hitBox.setX(xPos);
        }
    }

}
