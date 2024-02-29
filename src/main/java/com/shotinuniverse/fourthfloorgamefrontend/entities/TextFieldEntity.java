package com.shotinuniverse.fourthfloorgamefrontend.entities;

public class TextFieldEntity extends ElementField {

    private String text;
    private int editable;
    private boolean havePresentation;
    private String presentation;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public boolean isHavePresentation() {
        return havePresentation;
    }

    public void setHavePresentation(boolean havePresentation) {
        this.havePresentation = havePresentation;
    }
}
