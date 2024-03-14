package com.shotinuniverse.fourthfloorgamefrontend.entities;

public class AnimationEntity {

    private int id; // _id
    private String type;
    private int frameNumber; // frame_number
    private String action;
    private int value;
    private int ownerId; // object_id
    private String ownerClass; // class
    private int inMove; // in_move

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(String ownerClass) {
        this.ownerClass = ownerClass;
    }

    public int getInMove() {
        return inMove;
    }

    public void setInMove(int inMove) {
        this.inMove = inMove;
    }
}
