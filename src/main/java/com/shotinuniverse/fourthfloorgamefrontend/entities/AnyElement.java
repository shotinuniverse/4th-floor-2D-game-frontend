package com.shotinuniverse.fourthfloorgamefrontend.entities;

public abstract class AnyElement extends Points {

    private int id; // _id
    private String dbClass; // _class
    private int owner; // _owner
    private int order;
    private String style;

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

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
