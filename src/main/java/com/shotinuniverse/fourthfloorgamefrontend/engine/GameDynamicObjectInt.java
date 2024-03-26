package com.shotinuniverse.fourthfloorgamefrontend.engine;

import javafx.geometry.Side;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDynamicObjectInt {
    void setPhysic(int objectId) throws SQLException;
    void collisionHandler(ArrayList<LevelPlatform> platformArrayList);
    double calculateRunXByT(double beginX, double speedX, double accelerationX);
    double calculateJumpXByT(double beginX, double speedX);
    double calculateJumpYByT(double beginY, double speedY);
    void changePositionObjectAtCollision(ArrayList<LevelPlatform> platformArrayList);
    double checkHorizontalCollisionOnSegment(Rectangle hitBox, Rectangle hitBoxHead, ArrayList<LevelPlatform> platformArrayList);
}
