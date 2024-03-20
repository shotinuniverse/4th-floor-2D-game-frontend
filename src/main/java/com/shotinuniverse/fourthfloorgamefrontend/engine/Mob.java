package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Set;

public class Mob extends GameDynamicObject {
    public int limitFramesJump = 10;
    Side sideJump;

    public Mob(List<Rectangle> rectangleList) {
        super(rectangleList, 1);
    }

    public void move(Set<KeyCode> keyCodes) {

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
            Game.runnable = false;
            Game.gameThread.interrupt();
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
