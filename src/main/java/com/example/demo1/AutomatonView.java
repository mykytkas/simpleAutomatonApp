package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AutomatonView {
    @FXML
    Label labelNumberOutput;

    public void displayNumber(int number){
        labelNumberOutput.setText("" + number);
    }

}
