package com.shotinuniverse.fourthfloorgamefrontend.entities;

public class PlatformEntity extends Points {

    private int id; // _id
    private String dbClass; // _class
    private String image;
    private String  owner; // _owner
    private int visible;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDbClass() {
        return dbClass;
    }

    public void setDbClass(String dbClass) {
        this.dbClass = dbClass;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }
}
