package com.shotinuniverse.fourthfloorgamefrontend.entities;

public class SliderEntity extends ElementField {

    private int currentValue;
    private int min;
    private int max;
    private int majorTickUnit;
    private int minorTickCount;

    public SliderEntity(int min, int max, int majorTickUnit, int minorTickCount) {

        this.min = min;
        this.max = max;
        this.majorTickUnit = majorTickUnit;
        this.minorTickCount = minorTickCount;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMajorTickUnit() {
        return majorTickUnit;
    }

    public void setMajorTickUnit(int majorTickUnit) {
        this.majorTickUnit = majorTickUnit;
    }

    public int getMinorTickCount() {
        return minorTickCount;
    }

    public void setMinorTickCount(int minorTickCount) {
        this.minorTickCount = minorTickCount;
    }
}
