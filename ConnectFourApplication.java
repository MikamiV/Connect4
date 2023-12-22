package com.example.csce314ffl;

import javafx.application.Application;
import javafx.stage.Stage;

public class ConnectFourApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Initialize the game board logic
        GameBoard gameBoard = new GameBoard();

        // Initialize the AI logic with the game board
        AI ai = new AI(gameBoard);

        // Initialize the UI with the game board, AI, and primary stage
        GameUI gameUI = new GameUI(gameBoard, ai, primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}



