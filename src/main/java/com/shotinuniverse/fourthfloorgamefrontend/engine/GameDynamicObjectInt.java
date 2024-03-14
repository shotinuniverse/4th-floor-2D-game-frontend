package com.shotinuniverse.fourthfloorgamefrontend.engine;

import java.sql.SQLException;
import java.util.ArrayList;

public interface GameDynamicObjectInt {
    void setPhysic(int objectId) throws SQLException;
    void collisionHandler(ArrayList<LevelPlatform> platformArrayList);
    void setGravity(double gravity);
    double calculateGravity(double beginX, double beginY, int speedX, int speedY);
    double checkCollisionsWithPlatforms(ArrayList<LevelPlatform> platformArrayList);
}
