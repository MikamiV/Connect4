package com.example.csce314ffl;

import java.util.Random;

public class AI {

    private GameBoard gameBoard;
    private Random random;

    public AI(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.random = new Random();
    }

    public void playMove() {
        if (!playWinningMove() && !playBlockingMove()) {
            playRandomMove();
        }
    }

    private boolean playWinningMove() {
        for (int col = 0; col < gameBoard.getColumns(); col++) {
            int row = findAvailableRow(col);
            if (row != -1 && gameBoard.isWinningMove(row, col, Chip.AI)) {
                gameBoard.dropChip(col, Chip.AI);
                return true;
            }
        }
        return false;
    }

    private boolean playBlockingMove() {
        for (int col = 0; col < gameBoard.getColumns(); col++) {
            int row = findAvailableRow(col);
            if (row != -1 && gameBoard.isWinningMove(row, col, Chip.PLAYER)) {
                gameBoard.dropChip(col, Chip.AI);
                return true;
            }
        }
        return false;
    }

    private void playRandomMove() {
        int col;
        do {
            col = random.nextInt(gameBoard.getColumns());
        } while (!isColumnPlayable(col));
        gameBoard.dropChip(col, Chip.AI);
    }

    private boolean isColumnPlayable(int col) {
        return gameBoard.getBoardChip(0, col) == Chip.EMPTY;
    }

    private int findAvailableRow(int col) {
        for (int row = gameBoard.getRows() - 1; row >= 0; row--) {
            if (gameBoard.getBoardChip(row, col) == Chip.EMPTY) {
                return row;
            }
        }
        return -1; // Column is full
    }

}
