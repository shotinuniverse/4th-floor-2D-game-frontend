package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.SqlQuery;
import javafx.geometry.Bounds;
import javafx.geometry.Side;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shotinuniverse.fourthfloorgamefrontend.Game.runnable;

public class GameDynamicObject implements GameDynamicObjectInt {
    public List<Rectangle> hitBoxes;
    public int speedX;
    public int speedY;
    public int numberFrameEndJump;
    public boolean onGround;
    public boolean inMove;

    public GameDynamicObject(List<Rectangle> rectangleList, int objectId) {
        this.hitBoxes = rectangleList;
        this.numberFrameEndJump = 0;
        this.onGround = false;

        try {
            setPhysic(objectId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setPhysic(int objectId) throws SQLException {
        String query = String.format("""
                select
                    physic_properties.*
                from
                    physic_properties as physic_properties
                where
                    _id = %d
                """, objectId);

        Object object = SqlQuery.getObjectFromTable(query);
        Map<String, Object> map = (HashMap) object;
        speedX = (int) map.get("speedX");
        speedY = (int) map.get("speedY");
    }

    @Override
    public void collisionHandler(ArrayList<LevelPlatform> platformArrayList) {
        double gravity = checkCollisionsWithPlatforms(platformArrayList);
        setGravity(gravity);
    }

    @Override
    public void setGravity(double gravity) {
        if (onGround && gravity == PhysicConst.characterGravity)
            return;
        else if (numberFrameEndJump != 0)
            return;

        double yPos;

        for (int i = 0; i < hitBoxes.size(); i++) {
            Rectangle hitBox = hitBoxes.get(i);
            yPos = hitBox.getY() + gravity;
            if (yPos > 1080) {
                runnable = false;
                Thread.currentThread().stop();
            } else {
                hitBox.setY(yPos);
            }
        }
    }

    @Override
    public double calculateGravity(double beginX, double beginY, int speedX, int speedY) {
        return 0;
    }

    @Override
    public double checkCollisionsWithPlatforms(ArrayList<LevelPlatform> platformArrayList) {
        boolean collisionDetected = false;
        double gravity;
        if(numberFrameEndJump == 0) {
            Rectangle foot = hitBoxes.get(0);
            gravity = checkHorizontalCollisionOnSegment(foot, platformArrayList, Side.BOTTOM);
            if (gravity != PhysicConst.characterGravity)
                collisionDetected = true;
        } else {
            Rectangle head = hitBoxes.get(4);
            gravity = checkHorizontalCollisionOnSegment(head, platformArrayList, Side.TOP);
            if (gravity > 0 && gravity != PhysicConst.characterGravity)
                collisionDetected = true;
        }

        onGround = collisionDetected;
        return gravity;
    }

    public double checkHorizontalCollisionOnSegment(Rectangle hitBox,
                                                    ArrayList<LevelPlatform> platformArrayList,
                                                    Side side) {
        Bounds bounds = hitBox.getBoundsInParent();
        double x1 = bounds.getMinX();
        double x2 = bounds.getMaxX();
        double y;
        double gravity;
        if (side == Side.BOTTOM) {
            gravity = PhysicConst.characterGravity;
            y = bounds.getMaxY();
        } else {
            gravity = -1;
            y = bounds.getMinY();
        }

        for (LevelPlatform levelPlatform : platformArrayList) {
            Bounds platformBounds = levelPlatform.getHitBox().getBoundsInParent();
            double x1Platform = platformBounds.getMinX();
            double x2Platform = platformBounds.getMaxX();
            double yPlatform;
            if (side == Side.BOTTOM) {
                yPlatform = platformBounds.getMinY();
            } else {
                yPlatform = platformBounds.getMaxY();
            }

            if ((x1Platform <= x1 && x2 <= x2Platform)
                    || (x1 <= x1Platform && x2 >= x1Platform)
                    || (x1 <= x2Platform && x2 >= x2Platform)) {
                if (side == Side.BOTTOM) {
                    if ((y <= yPlatform || Math.floor(y) == yPlatform) && (y + 1 == yPlatform || y + gravity >= yPlatform)) {
                        gravity = yPlatform - y;
                        break;
                    }
                } else {
                    gravity = PhysicConst.characterGravity;
                    if (y >= yPlatform && (Math.round(y) - 1 == yPlatform || y - speedY <= yPlatform)) {
                        numberFrameEndJump = Game.getCurrentFrame() + 1;
                        break;
                    }
                }
            }
        }

        return gravity;
    }
}