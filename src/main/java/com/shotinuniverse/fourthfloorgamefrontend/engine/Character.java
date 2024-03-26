package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class Character extends GameDynamicObject {
    Side sideJump;

    public Character(List<Rectangle> rectangleList) {
        super(rectangleList, 1, 10);
    }

    public void characterKeyHandler() {
        Set<KeyCode> keyCodes = getActiveKeys();
        handleServiceKeyPressed(keyCodes);

        if (!onGround){
            inMove = false;
            if (counterFrameJump != 0 ) {
//            if (numberFrameEndJump != 0) {
                endJump(sideJump);
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
                beginJump(hitBox, sideJump);
                if(i == countHitBoxes){
                    onGround = false;
                    inMove = true;
                }
            }
            else if (pressedLeft(keyCodes) && pressedUp(keyCodes)) {
                sideJump = Side.LEFT;
                beginJump(hitBox, sideJump);
                if(i == countHitBoxes){
                    onGround = false;
                    inMove = true;
                }
            }
            else if (pressedUp(keyCodes)) {
                sideJump = Side.TOP;
                beginJump(hitBox, sideJump);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (pressedRight(keyCodes)) {
                xPos = hitBox.getX() + speedX;
                hitBox.setX(xPos);
                if(i == countHitBoxes)
                    inMove = true;
            }
            else if (pressedLeft(keyCodes)) {
                xPos = hitBox.getX() - speedX;
                hitBox.setX(xPos);
                if(i == countHitBoxes)
                    inMove = true;
            }
        }
    }

}
