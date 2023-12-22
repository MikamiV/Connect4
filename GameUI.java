package com.example.csce314ffl;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameUI {

    private GameBoard gameBoard;
    private AI ai;
    private Stage primaryStage;
    private GridPane grid;

    public GameUI(GameBoard gameBoard, AI ai, Stage primaryStage) {
        this.gameBoard = gameBoard;
        this.ai = ai;
        this.primaryStage = primaryStage;
        this.grid = new GridPane();
        initializeBoard();
    }

    private void initializeBoard() {
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, null)));

        for (int row = 0; row < gameBoard.getRows(); row++) {
            for (int col = 0; col < gameBoard.getColumns(); col++) {
                Button button = createButton(col);
                grid.add(button, col, row);
            }
        }

        addControlButtons();

        Scene scene = new Scene(grid);
        primaryStage.setTitle("Connect Four");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createButton(int col) {
        Button button = new Button();
        button.setMinSize(50, 50);
        button.setMaxSize(50, 50);
        button.setStyle("-fx-background-radius: 25; -fx-background-color: #FFFFFF;");
        button.setOnAction(event -> handleButtonClick(col));
        return button;
    }

    private void addControlButtons() {
        // Save button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> gameBoard.saveGame("game_save.dat"));
        grid.add(saveButton, gameBoard.getColumns(), 0);

        // Load button
        Button loadButton = new Button("Load");
        loadButton.setOnAction(event -> {
            gameBoard.loadGame("game_save.dat");
            updateBoard();
        });
        grid.add(loadButton, gameBoard.getColumns(), 1);

        // Reset button
        Button resetButton = new Button("Reset");
        resetButton.setOnAction(event -> resetGame());
        grid.add(resetButton, gameBoard.getColumns(), 2);
    }

    private void handleButtonClick(int col) {
        Platform.runLater(() -> {
            if (gameBoard.dropChip(col, Chip.PLAYER)) {
                updateBoard();
                if (gameBoard.checkForWinner()) {
                    showAlert("Player wins!");
                    resetGame();
                } else if (gameBoard.isBoardFull()) {
                    showAlert("Stalemate! The game is a draw.");
                    resetGame();
                } else {
                    ai.playMove(); // Let AI make a move
                    updateBoard();
                    if (gameBoard.checkForWinner()) {
                        showAlert("AI wins!");
                        resetGame();
                    } else if (gameBoard.isBoardFull()) {
                        showAlert("Stalemate! The game is a draw.");
                        resetGame();
                    }
                }
            } else {
                showAlert("Column is full. Try a different one.");
            }
        });
    }


    private void updateBoard() {
        for (int row = 0; row < gameBoard.getRows(); row++) {
            for (int col = 0; col < gameBoard.getColumns(); col++) {
                Button button = (Button) grid.getChildren().get(row * gameBoard.getColumns() + col);
                Chip chip = gameBoard.getBoardChip(row, col);
                if (chip == Chip.PLAYER) {
                    button.setStyle("-fx-background-radius: 25; -fx-background-color: #FFFF00; -fx-border-color: #000000;");
                } else if (chip == Chip.AI) {
                    button.setStyle("-fx-background-radius: 25; -fx-background-color: #FF0000; -fx-border-color: #000000;");
                } else {
                    button.setStyle("-fx-background-radius: 25; -fx-background-color: #FFFFFF; -fx-border-color: #000000;");
                }
            }
        }
    }

    private void resetGame() {
        gameBoard.resetBoard();
        updateBoard();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Connect Four");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
