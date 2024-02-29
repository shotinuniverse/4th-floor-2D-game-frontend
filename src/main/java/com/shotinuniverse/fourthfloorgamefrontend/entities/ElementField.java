package com.shotinuniverse.fourthfloorgamefrontend.entities;

public class ElementField extends AnyElement {

    private String valueTable; // table_data
    private String valueColumn; // column_data
    private int valueRowId; // static_id

    public String getValueTable() {
        return valueTable;
    }

    public void setValueTable(String valueTable) {
        this.valueTable = valueTable;
    }

    public String getValueColumn() {
        return valueColumn;
    }

    public void setValueColumn(String valueColumn) {
        this.valueColumn = valueColumn;
    }

    public int getValueRowId() {
        return valueRowId;
    }

    public void setValueRowId(int valueRowId) {
        this.valueRowId = valueRowId;
    }
}
