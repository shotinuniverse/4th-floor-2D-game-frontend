package com.shotinuniverse.fourthfloorgamefrontend.entities;

public abstract class Points implements Position{
    private int pointX;
    private int pointY;
    private int height;
    private int width;

    public void setPointX (int value) {
        pointX = value;
    }

    public void setPointY (int value) {
        pointY = value;
    }

    public void setHeight (int value) {
        height = value;
    }

    public void setWidth (int value) {
        width = value;
    }

    public int getPointX () {
        return pointX;
    }

    public int getPointY () {
        return pointY;
    }

    public int getHeight () {
        return height;
    }

    public int getWidth () {
        return width;
    }
}
