package com.shotinuniverse.fourthfloorgamefrontend.entities;

import javafx.collections.ObservableList;

public class ComboboxEntity extends ElementField {

    private ObservableList<String> availableValues;
    private String currentValue;

    public ObservableList<String> getAvailableValues() {
        return availableValues;
    }

    public void setAvailableValues(ObservableList<String> availableValues) {
        this.availableValues = availableValues;
    }

    public String getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        this.currentValue = currentValue;
    }
}
