package com.legadi.ui.vacations.common;

import org.apache.poi.ss.util.CellReference;

public class CellRef {

    private final String cell;
    private final String column;
    private final int col;
    private final int row;

    public CellRef(String cell) {
        this.cell = cell;
        this.column = cell.replaceAll("\\d+$", "");
        this.col = CellReference.convertColStringToIndex(column);
        // Based 0 row number
        this.row = Integer.parseInt(cell.replaceAll("^[a-zA-Z]+", "0")) - 1;
    }

    public String getCell() {
        return cell;
    }

    public String getColumn() {
        return column;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
}
