package com.shotinuniverse.fourthfloorgamefrontend.entities;

public class HitBoxEntity extends AnyElement{

    private int id; // _id
    private String ownerClass; // class
    private int ownerId; // object_id
    private String name;
    private int relativeX;
    private int relativeY;
    private int relativeWidth;
    private int relativeHeight;
    private String type;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(String ownerClass) {
        this.ownerClass = ownerClass;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getRelativeX() {
        return relativeX;
    }

    public void setRelativeX(int relativeX) {
        this.relativeX = relativeX;
    }

    public int getRelativeY() {
        return relativeY;
    }

    public void setRelativeY(int relativeY) {
        this.relativeY = relativeY;
    }

    public int getRelativeWidth() {
        return relativeWidth;
    }

    public void setRelativeWidth(int relativeWidth) {
        this.relativeWidth = relativeWidth;
    }

    public int getRelativeHeight() {
        return relativeHeight;
    }

    public void setRelativeHeight(int relativeHeight) {
        this.relativeHeight = relativeHeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
