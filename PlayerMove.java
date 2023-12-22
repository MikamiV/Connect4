package com.example.csce314ffl;

import java.io.Serializable;

public class PlayerMove implements Serializable {
    private final int row;
    private final int col;
    private final Chip chip;

    public PlayerMove(int row, int col, Chip chip) {
        this.row = row;
        this.col = col;
        this.chip = chip;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Chip getChip() {
        return chip;
    }
}

