package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private Slider slider;

    @FXML
    private Button button;

    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException {
        int number = (int)slider.getValue();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("automaton-view.fxml"));
        root = loader.load();
        AutomatonView automatonView = loader.getController();
        automatonView.displayNumber(number);

        //Parent root = FXMLLoader.load(getClass().getResource("automaton-view.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}