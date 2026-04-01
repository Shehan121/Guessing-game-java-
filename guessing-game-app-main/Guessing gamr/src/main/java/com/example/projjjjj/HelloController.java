package com.example.projjjjj;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;


import java.util.Random;



import javax.swing.*;

import java.io.IOException;

public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

     public void switchToScene2 (ActionEvent event) throws IOException{
         Parent root = FXMLLoader.load(getClass().getResource("new.fxml"));
         stage = (Stage)((Node)event.getSource()).getScene().getWindow();
         scene = new Scene(root);
         stage.setScene(scene);
         stage.show();
    }


    @FXML
    private TextField guessField;

    @FXML
    private Label feedbackLabel;

    private int targetNumber;
    private final int maxNumber = 100;

    public void initialize() {
        resetGame();
    }

    @FXML
    private void handleSubmitGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            if (guess < 1 || guess > maxNumber) {
                feedbackLabel.setText("Please enter a number between 1 and " + maxNumber + "!");
            } else if (guess < targetNumber) {
                feedbackLabel.setText("Too Low!");
            } else if (guess > targetNumber) {
                feedbackLabel.setText("Too High!");
            } else {
                feedbackLabel.setText("You Win! The number was " + targetNumber);
            }
        } catch (NumberFormatException e) {
            feedbackLabel.setText("Please enter a valid number!");
        }
    }

    @FXML
    private void handleResetGame() {
        resetGame();
        feedbackLabel.setText("");
        guessField.clear();
    }

    private void resetGame() {
        Random random = new Random();
        targetNumber = random.nextInt(maxNumber) + 1; // Generate number between 1 and maxNumber
    }
}








