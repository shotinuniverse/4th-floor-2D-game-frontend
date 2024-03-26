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
    Side sideJump;
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
        onGround = false;
        changePositionObjectAtCollision(platformArrayList);
    }

    @Override
    public double calculateRunXByT(double beginX, double speedX, double accelerationX) {
        int frame = Math.min(counterFrameRun, limitFramesAcceleration);
        double accByT = accelerationX * Math.pow(frame, 2);
        return beginX + speedX * frame + accByT / 2;
    }

    @Override
    public double calculateJumpXByT(double beginX, double speedX) {
        // X(t) = x0 + v0 * cos Alfa * t
        return beginX + speedX;
    }

    @Override
    public double calculateJumpYByT(double beginY, double speedY) {
        // Y(t) = y0 + v0 * sin Alfa * t - gt^2/2
        int frame = Math.min(counterFrameJump, limitFramesJump);
        double gByT = 0.1 * Math.pow(counterFrameJump, 2);
        double yByT = beginY - speedY * frame + gByT / 2;
        return yByT;
    }

    @Override
    public void changePositionObjectAtCollision(ArrayList<LevelPlatform> platformArrayList) {
        Rectangle foot = hitBoxes.get(0);
        Rectangle head = hitBoxes.get(4);

        double gravity = checkHorizontalCollisionOnSegment(foot, head, platformArrayList);
        if (gravity == 0 && counterFrameJump == 0 && !onGround) {
            sideJump = Side.TOP;
            gravity = calculateJumpYByT(0, 0);
            counterFrameJump = limitFramesJump + 1;
        }

        if (gravity != 0) {
            for (int i = 0; i < hitBoxes.size(); i++) {
                Rectangle hitBox = hitBoxes.get(i);
                double yPos = hitBox.getY() + gravity;
                hitBox.setY(yPos);
            }
        }
    }

    @Override
    public double checkHorizontalCollisionOnSegment(Rectangle hitBoxFoot, Rectangle hitBoxHead,
                                                    ArrayList<LevelPlatform> platformArrayList) {
        Bounds boundsFoot = hitBoxFoot.getBoundsInParent();
        double x1Foot = boundsFoot.getMinX();
        double x2Foot = boundsFoot.getMaxX();
        double yMaxFoot = boundsFoot.getMaxY();

        Bounds boundsHead = hitBoxHead.getBoundsInParent();
        double x1Head = boundsHead.getMinX();
        double x2Head = boundsHead.getMaxX();
        double yMinHead = boundsHead.getMinY();

        double newPotentialYFoot = calculateJumpYByT(yMaxFoot, 1.2);
        double newPotentialYHead = calculateJumpYByT(yMinHead, 1.2);
        double gravity = 0;

        for (LevelPlatform levelPlatform : platformArrayList) {
            Bounds platformBounds = levelPlatform.getHitBox().getBoundsInParent();
            double x1Platform = platformBounds.getMinX();
            double x2Platform = platformBounds.getMaxX();
            double yPlatformMin = platformBounds.getMinY();
            double yPlatformMax = platformBounds.getMaxY();

            if (((x1Platform <= x1Foot && x2Foot <= x2Platform)
                    || (x1Foot <= x1Platform && x2Foot >= x1Platform)
                    || (x1Foot <= x2Platform && x2Foot >= x2Platform))
                    && (yMaxFoot <= yPlatformMin || (yMaxFoot - 3 < yPlatformMin && yPlatformMin < yMaxFoot + 3))
                    && (yMaxFoot + 1 == yPlatformMin || newPotentialYFoot >= yPlatformMin)) {
                gravity = yPlatformMin - yMaxFoot;
                counterFrameJump = 0;
                onGround = true;
                break;
            } else if (((x1Platform <= x1Head && x2Head <= x2Platform)
                    || (x1Head <= x1Platform && x2Head >= x1Platform)
                    || (x1Head <= x2Platform && x2Head >= x2Platform))
                    && yMinHead >= yPlatformMax && newPotentialYHead <= yPlatformMax) {
                gravity = calculateJumpYByT(0, 1.2);
                counterFrameJump = limitFramesJump + 1;
                break;
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

        double xByT = calculateRunXByT(hitBox.getX(), moveX, accelerationX);
        hitBox.setX(xByT);
    }

    public void beginJump(Rectangle hitBox) {
        if (!onGround) {
            return;
        }

        if (counterFrameJump == 0) {
            counterFrameJump = 1;
        }

        editCoordinatesInJump(hitBox);
    }

    public void endJump() {
        if (counterFrameJump == 0)
            return;

        // прыжок уже начат в первом кадре
        if (counterFrameJump == 1)
            counterFrameJump = 2;

        for (Rectangle hitBox: hitBoxes) {
            editCoordinatesInJump(hitBox);
        }

        counterFrameJump += 1;
    }

    private void editCoordinatesInJump(Rectangle hitBox) {
        double moveX = speedX * 2;
        switch (sideJump) {
            case LEFT -> moveX = -moveX;
            case TOP -> moveX = 0;
        }

        double moveY = 1.2;

        double xPos = calculateJumpXByT(hitBox.getX(), moveX);
        double yPos = calculateJumpYByT(hitBox.getY(), moveY);

        hitBox.setX(xPos);
        hitBox.setY(yPos);
    }

}