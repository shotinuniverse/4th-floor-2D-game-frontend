package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.entities.LevelEntity;
import com.shotinuniverse.fourthfloorgamefrontend.entities.PlatformEntity;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class LevelPlatform {

    private Rectangle hitBox;

    public LevelPlatform(Rectangle rectangle) {
        this.hitBox = rectangle;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }
}
