package com.example.demo1;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    private Slider slider;

    @FXML
    private Button button;

    private int number;
    private Parent root;
    private Stage stage;
    private Scene scene;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                number = (int) slider.getValue();
                welcomeText.setText("Selected: " + number + " nodes");
            }
        });
    }


    @FXML
    protected void onHelloButtonClick(ActionEvent event) throws IOException {
        number = (int)slider.getValue();

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