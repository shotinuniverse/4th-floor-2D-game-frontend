package com.shotinuniverse.fourthfloorgamefrontend.engine;

import com.shotinuniverse.fourthfloorgamefrontend.Game;
import com.shotinuniverse.fourthfloorgamefrontend.common.KeyHandler;
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

public class GameDynamicObject extends KeyHandler implements GameDynamicObjectInt {
    public List<Rectangle> hitBoxes;
    public int speedX;
    public int speedY;
    //public int numberFrameEndJump;
    public int counterFrameJump;
    public int counterFrameRun;
    public boolean onGround;
    public boolean inMove;
    public int limitFramesJump;
    public double acceleration;
    public int limitFramesAcceleration;

    public GameDynamicObject(List<Rectangle> rectangleList,
                             int objectId, int limitFramesJump, int limitFramesAcceleration) {
        super(true);

        this.hitBoxes = rectangleList;
        //this.numberFrameEndJump = 0;
        this.counterFrameJump = 0;
        this.counterFrameRun = 0;
        this.onGround = false;
        this.limitFramesJump = limitFramesJump;
        this.acceleration = 0.05;
        this.limitFramesAcceleration = limitFramesAcceleration;

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

        double yPos;

        for (int i = 0; i < hitBoxes.size(); i++) {
            Rectangle hitBox = hitBoxes.get(i);
            yPos = hitBox.getY() + gravity;
            hitBox.setY(yPos);
        }
    }

    @Override
    public double calculateRunXByT(int currentFrameAction, double beginX, double speedX, double accelerationX) {
        int frame = Math.min(currentFrameAction, limitFramesAcceleration);
        double accByT = accelerationX * Math.pow(frame, 2);
        return beginX + speedX * frame + accByT / 2;
    }

    @Override
    public double calculateJumpXByT(int currentFrameAction, double beginX, double speedX) {
        // X(t) = x0 + v0 * cos Alfa * t
        return beginX + speedX;
    }

    @Override
    public double calculateJumpYByT(int currentFrameAction, double beginY, double speedY) {
        // Y(t) = y0 + v0 * sin Alfa * t - gt^2/2
        int frame = Math.min(currentFrameAction, limitFramesJump);
        double gByT = 0.1 * Math.pow(currentFrameAction, 2);
        double yByT = beginY - speedY * frame + gByT / 2;
        return yByT;
    }

    @Override
    public double checkCollisionsWithPlatforms(ArrayList<LevelPlatform> platformArrayList) {
        boolean collisionDetected = false;
        double gravity;
        //if(numberFrameEndJump == 0) {
        if(counterFrameJump == 0) {
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

    @Override
    public double checkHorizontalCollisionOnSegment(Rectangle hitBox,
                                                    ArrayList<LevelPlatform> platformArrayList,
                                                    Side side) {
        Bounds bounds = hitBox.getBoundsInParent();
        double x1 = bounds.getMinX();
        double x2 = bounds.getMaxX();
        double y;
//        double gravity;
//        if (side == Side.BOTTOM) {
//            gravity = PhysicConst.characterGravity;
//            y = bounds.getMaxY();
//        } else {
//            gravity = -1;
//            y = bounds.getMinY();
//        }
//
//        for (LevelPlatform levelPlatform : platformArrayList) {
//            Bounds platformBounds = levelPlatform.getHitBox().getBoundsInParent();
//            double x1Platform = platformBounds.getMinX();
//            double x2Platform = platformBounds.getMaxX();
//            double yPlatform;
//            if (side == Side.BOTTOM) {
//                yPlatform = platformBounds.getMinY();
//            } else {
//                yPlatform = platformBounds.getMaxY();
//            }
//
//            if ((x1Platform <= x1 && x2 <= x2Platform)
//                    || (x1 <= x1Platform && x2 >= x1Platform)
//                    || (x1 <= x2Platform && x2 >= x2Platform)) {
//                if (side == Side.BOTTOM) {
//                    if ((y <= yPlatform || Math.floor(y) == yPlatform) && (y + 1 == yPlatform || y + gravity >= yPlatform)) {
//                        gravity = yPlatform - y;
//                        break;
//                    }
//                } else {
//                    gravity = PhysicConst.characterGravity;
//                    if (y >= yPlatform && (Math.round(y) - 1 == yPlatform || y - speedY <= yPlatform)) {
//                        //numberFrameEndJump = Game.getCurrentFrame() + 1;
//                        counterFrameJump = limitFramesJump;
//                        break;
//                    }
//                }
//            }
//        }
        double gByT;
        double gravity = 0;
        if (side == Side.BOTTOM) {
            //gravity = PhysicConst.characterGravity;
            gByT = 0.1 * Math.pow(counterFrameJump, 2);
            y = bounds.getMaxY();
        } else {
            gByT = -1;
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
                    if ((y <= yPlatform || Math.floor(y) == yPlatform) && (y + 1 == yPlatform || y + gByT >= yPlatform)) {
                        gravity = yPlatform - y;
                        break;
                    }
                } else {
                    gravity = PhysicConst.characterGravity;
                    if (y >= yPlatform && (Math.round(y) - 1 == yPlatform || y - speedY <= yPlatform)) {
                        //numberFrameEndJump = Game.getCurrentFrame() + 1;
                        counterFrameJump = limitFramesJump;
                        break;
                    }
                }
            }
        }

        return gravity;
    }

    public void running(Rectangle hitBox, Side sideMove) {
        if (counterFrameRun == 0)
            counterFrameRun = 1;

        double moveX;
        double accelerationX;
        if (sideMove == Side.LEFT) {
            moveX = -speedX * 0.2;
            accelerationX = -acceleration;
        } else {
            moveX = speedX * 0.2;
            accelerationX = acceleration;
        }

        double xByT = calculateRunXByT(counterFrameRun, hitBox.getX(), moveX, accelerationX);
        hitBox.setX(xByT);
    }

    public void beginJump(Rectangle hitBox, Side sideJump) {
        if (!onGround) {
            return;
        }

        if (counterFrameJump == 0) {
            counterFrameJump = 1;
        }

        editCoordinatesInJump(hitBox, sideJump);
    }

    public void endJump(Side sideJump) {
        if (counterFrameJump == 0)
            return;

        counterFrameJump += 1;

        for (Rectangle hitBox: hitBoxes) {
            editCoordinatesInJump(hitBox, sideJump);
        }
    }

    private void editCoordinatesInJump(Rectangle hitBox, Side sideJump) {
        double moveX = speedX * 2;
        switch (sideJump) {
            case LEFT -> moveX = -moveX;
            case TOP -> moveX = 0;
        }

        double moveY = 1.2;

        double xPos = calculateJumpXByT(counterFrameJump, hitBox.getX(), moveX);
        double yPos = calculateJumpYByT(counterFrameJump, hitBox.getY(), moveY);

        hitBox.setX(xPos);
        hitBox.setY(yPos);
    }

}