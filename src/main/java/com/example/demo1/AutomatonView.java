package com.example.demo1;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class AutomatonView {
    @FXML
    private Button addButton;

    @FXML
    private TextField charsField;

    @FXML
    private Button checkButton;

    @FXML
    private ChoiceBox<?> choiceFrom;

    @FXML
    private ChoiceBox<?> choiceTo;

    @FXML
    private TextField stringField;

    @FXML
    private AnchorPane pane;

    private ArrayList<Circle> circles = new ArrayList<>();
    int oldX = 165;
    int oldY;
    int radius = 40;
    public void displayCircles(int number){

        for (int i = 0; i < number; i++) {
            Circle circle = new Circle();
            circle.setCenterX(60*i + radius);
            circle.setCenterY(60*i + radius);
            circle.setRadius(radius);
            //circle.setOnMouseClicked(this::onRightClick);
            circle.setOnMouseDragged(this::onMouseDragged);
            circles.add(circle);
            pane.getChildren().add(circle);
        }
    }

    @FXML
    protected void onMouseDragged(MouseEvent event) {
        int x = (int)event.getSceneX() - oldX;
        if (event.getSceneX() < pane.getPrefWidth() + pane.getLayoutX() - radius && event.getSceneX() > pane.getLayoutX() + radius){
            ((Circle)event.getSource()).setCenterX(x);

        }
        int y = (int)event.getSceneY() - oldY;
        if (event.getSceneY() < pane.getPrefHeight() - radius && event.getSceneY() > radius){
            ((Circle)event.getSource()).setCenterY(y);

        }
    }



}
