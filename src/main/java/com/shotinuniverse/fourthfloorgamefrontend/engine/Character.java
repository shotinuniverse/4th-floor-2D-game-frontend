package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.util.*;

public class Character extends GameDynamicObject {

    public Character(List<Rectangle> rectangleList) {
        super(rectangleList, 1, 20, 10);
    }

    public void characterKeyHandler() {
        Set<KeyCode> keyCodes = getActiveKeys();
        handleServiceKeyPressed(keyCodes);

        if (!onGround){
            inMove = false;
            if (counterFrameJump != 0 ) {
//            if (numberFrameEndJump != 0) {
                endJump();
            }
            return;
        }

        if (keyCodes.size() == 0) {
            inMove = false;
            counterFrameRun = 0;
            return;
        }

        move(keyCodes);
    }

    public void move(Set<KeyCode> keyCodes) {
        int countHitBoxes = hitBoxes.size() - 1;
        for (int i = 0; i <= countHitBoxes; i++) {
            Rectangle hitBox = hitBoxes.get(i);
            if (pressedRight(keyCodes) && pressedUp(keyCodes)) {
                sideJump = Side.RIGHT;
                beginJump(hitBox);
                if(i == countHitBoxes){
                    onGround = false;
                    inMove = true;
                }
            }
            else if (pressedLeft(keyCodes) && pressedUp(keyCodes)) {
                sideJump = Side.LEFT;
                beginJump(hitBox);
                if(i == countHitBoxes){
                    onGround = false;
                    inMove = true;
                }
            }
            else if (pressedUp(keyCodes)) {
                sideJump = Side.TOP;
                beginJump(hitBox);
                if(i == countHitBoxes)
                    onGround = false;
            }
            else if (pressedRight(keyCodes)) {
                running(hitBox, Side.RIGHT);
                if(i == countHitBoxes) {
                    inMove = true;
                    counterFrameRun += 1;
                }
            }
            else if (pressedLeft(keyCodes)) {
                running(hitBox, Side.LEFT);
                if(i == countHitBoxes) {
                    inMove = true;
                    counterFrameRun += 1;
                }
            }
        }
    }

}
