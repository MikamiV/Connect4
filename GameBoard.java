package com.example.csce314ffl;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class GameBoard {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;

    private List<Chip> board = new ArrayList<>(ROWS * COLUMNS);
    private List<PlayerMove> moveLog = new ArrayList<>();

    public GameBoard() {
        resetBoard();
    }

    public void resetBoard() {
        board.clear();
        for (int i = 0; i < ROWS * COLUMNS; i++) {
            board.add(Chip.EMPTY);
        }
        moveLog.clear();
    }

    private Chip getChip(int row, int col) {
        return board.get(row * COLUMNS + col);
    }

    private void setChip(int row, int col, Chip chip) {
        board.set(row * COLUMNS + col, chip);
    }

    public boolean dropChip(int col, Chip chip) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (getChip(row, col) == Chip.EMPTY) {
                setChip(row, col, chip);
                PlayerMove move = new PlayerMove(row, col, chip);
                moveLog.add(move);
                return true;
            }
        }
        return false;
    }
    public PlayerMove getLastMove() {
        if (!moveLog.isEmpty()) {
            return moveLog.get(moveLog.size() - 1);
        }
        return null;
    }

    public Chip getBoardChip(int row, int col) {
        int index = row * COLUMNS + col;
        if (index >= 0 && index < board.size()) {
            return board.get(index);
        }
        return Chip.EMPTY;
    }
    public boolean isWinningMove(int row, int col, Chip chip) {
        setChip(row, col, chip);

        // Check for a win
        boolean won = checkForWinner(row, col);

        setChip(row, col, Chip.EMPTY);

        return won;
    }

    public boolean checkForWinner() {
        // Logic to check the entire board for a winner
        for (PlayerMove move : moveLog) {
            if (checkForWinner(move.getRow(), move.getCol())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkForWinner(int row, int col) {
        Chip chip = getChip(row, col);
        if (chip == Chip.EMPTY) {
            return false;
        }

        return checkDirection(row, col, 1, 0, chip) ||  // Horizontal
                checkDirection(row, col, 0, 1, chip) ||  // Vertical
                checkDirection(row, col, 1, 1, chip) ||  // Diagonal (down-right)
                checkDirection(row, col, 1, -1, chip);   // Diagonal (up-right)
    }

    private boolean checkDirection(int row, int col, int rowDirection, int colDirection, Chip chip) {
        int count = 0;

        for (int i = -3; i <= 3; i++) {
            int newRow = row + i * rowDirection;
            int newCol = col + i * colDirection;

            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS) {
                if (getChip(newRow, newCol) == chip) {
                    count++;
                    if (count == 4) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        return false;
    }

    public void saveGame(String fileName) {
        String contentToSave = moveLog.stream()
                .map(move -> move.getChip().toString() + ": " + move.getRow() + ", " + move.getCol())
                .collect(Collectors.joining("\n")); // Format: "Player/AI: Row#, Column#"
        try {
            Files.write(Paths.get(fileName), contentToSave.getBytes());
            System.out.println("Game saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving game: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadGame(String fileName) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            resetBoard();
            for (String line : lines) {
                String[] parts = line.split(": ");
                String[] coordinates = parts[1].split(", ");
                Chip chip = Chip.valueOf(parts[0]);
                int row = Integer.parseInt(coordinates[0]);
                int col = Integer.parseInt(coordinates[1]);
                // Replay the moves onto the board
                setChip(row, col, chip);
                moveLog.add(new PlayerMove(row, col, chip));
            }
            System.out.println("Game loaded successfully.");
        } catch (IOException e) {
            System.out.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void reconstructGameState() {
        resetBoard();
        for (PlayerMove move : moveLog) {
            setChip(move.getRow(), move.getCol(), move.getChip());
        }
    }

    public boolean isBoardFull() {
        return moveLog.size() == ROWS * COLUMNS;
    }

    public List<Chip> getBoard() {
        return board;
    }

    public List<PlayerMove> getMoveLog() {
        return moveLog;
    }

    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLUMNS;
    }

}
