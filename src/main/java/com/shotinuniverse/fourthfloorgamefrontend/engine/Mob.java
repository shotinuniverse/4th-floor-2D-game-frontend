package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import javafx.geometry.Side;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.Set;

public class Mob extends GameDynamicObject {
    Side sideJump;

    public Mob(List<Rectangle> rectangleList) {
        super(rectangleList, 1, 10);
    }

    public void move(Set<KeyCode> keyCodes) {

    }

}
