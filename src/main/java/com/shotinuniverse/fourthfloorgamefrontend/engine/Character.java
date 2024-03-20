package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import com.shotinuniverse.fourthfloorgamefrontend.menu.Pause;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.*;

public class Character extends GameDynamicObject {
    public int limitFramesJump = 10;
    Side sideJump;

    public Character(List<Rectangle> rectangleList) {
        super(rectangleList, 1);
    }

    public void characterKeyHandler() {
        Set<KeyCode> keyCodes = getActiveKeys();
        handleServiceKeyPressed(keyCodes);

        if (!onGround){
            inMove = false;
            if (numberFrameEndJump != 0) {
                endJump();
            }
            return;
        }

        if (keyCodes.size() == 0) {
            inMove = false;
            return;
        }

        move(keyCodes);
    }

    public void move(Set<KeyCode> keyCodes) {
        int countHitBoxes = hitBoxes.size() - 1;
        for (int i = 0; i <= countHitBoxes; i++) {
            Rectangle hitBox = hitBoxes.get(i);
            double xPos;
            if (pressedRight(keyCodes) && pressedUp(keyCodes)) {
                sideJump = Side.RIGHT;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (pressedLeft(keyCodes) && pressedUp(keyCodes)) {
                sideJump = Side.LEFT;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (pressedUp(keyCodes)) {
                sideJump = Side.TOP;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (pressedRight(keyCodes)) {
                xPos = hitBox.getX() + speedX;
                hitBox.setX(xPos);
            }
            else if (pressedLeft(keyCodes)) {
                xPos = hitBox.getX() - speedX;
                hitBox.setX(xPos);
            }
        }
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
