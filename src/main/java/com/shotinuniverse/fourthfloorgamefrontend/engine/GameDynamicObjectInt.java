package com.shotinuniverse.fourthfloorgamefrontend.engine;

import javafx.geometry.Side;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDynamicObjectInt {
    void setPhysic(int objectId) throws SQLException;
    void collisionHandler(ArrayList<LevelPlatform> platformArrayList);
    void setGravity(double gravity);
    double calculateJumpXByT(int currentFrame, double beginX, double speedX);
    double calculateJumpYByT(int currentFrame, double beginY, double speedY);
    double checkCollisionsWithPlatforms(ArrayList<LevelPlatform> platformArrayList);
    double checkHorizontalCollisionOnSegment(Rectangle hitBox, ArrayList<LevelPlatform> platformArrayList, Side side);
}
