package com.shotinuniverse.fourthfloorgamefrontend.entities;

import java.util.ArrayList;

public class LevelEntity {

    private int id; // _id
    private String dbClass; // _class
    private String name;
    private int number;
    private Points charPosition; // char_position
    private ArrayList<PlatformEntity> platformEntities;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Points getCharPosition() {
        return charPosition;
    }

    public void setCharPosition(Points charPosition) {
        this.charPosition = charPosition;
    }

    public ArrayList<PlatformEntity> getPlatformEntities() {
        return platformEntities;
    }

    public void setPlatformEntities(ArrayList<PlatformEntity> platformEntities) {
        this.platformEntities = platformEntities;
    }
}
