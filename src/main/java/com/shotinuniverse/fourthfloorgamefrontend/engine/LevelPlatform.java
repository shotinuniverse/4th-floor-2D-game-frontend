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

    public static List<Rectangle> getHitBoxesRectangles(LevelEntity levelEntity) {

        List<Rectangle> rectangleList = new ArrayList<>();

        ArrayList<PlatformEntity> platformEntities = levelEntity.getPlatformEntities();
        for (PlatformEntity entity: platformEntities) {
            Rectangle rectangle = new Rectangle(entity.getPointX(),
                    entity.getPointY(), entity.getWidth(), entity.getHeight());

            rectangle.setFill(Color.DARKGREEN);

            rectangleList.add(rectangle);
        }

        return rectangleList;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }
}
