package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;

import java.util.ArrayList;

public class AutomatonView {
    @FXML
    private Button addButton;

    @FXML
    private TextField charsField;

    @FXML
    private Button checkButton;

    @FXML
    private ChoiceBox<Integer> choiceFrom;

    @FXML
    private ChoiceBox<Integer> choiceTo;

    @FXML
    private TextField stringField;

    @FXML
    private AnchorPane pane;

    private ArrayList<Circle> circles = new ArrayList<>();
    private ArrayList<idCurve> curves = new ArrayList<>();
    private int oldX;
    private int oldY;
    final private int radius = 25;
    private int number;
    public void displayCircles(int number){
        this.number = number;
        oldX = (int)pane.getLayoutX();

        for (int i = 0; i < number; i++) {
            Circle circle = new Circle();
            circle.setCenterX(150*Math.cos((i+1)*2*Math.PI/number) + pane.getPrefWidth()/2);
            circle.setCenterY(150*Math.sin((i+1)*2*Math.PI/number) + pane.getPrefHeight()/2);
            circle.setRadius(radius);
            circle.setId(String.valueOf(i));
            circle.setFill(Color.WHITE);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(2);
            //circle.setOnMouseClicked(this::onRightClick);
            circle.setOnMouseDragged(this::onMouseDragged);
            circles.add(circle);
            pane.getChildren().add(circle);
        }
        for (int i = 0; i < circles.size(); i++){
            choiceTo.getItems().add(i);
            choiceFrom.getItems().add(i);
        }
        choiceTo.setValue(0);
        choiceFrom.setValue(0);
    }


    @FXML
    protected void addEdge(ActionEvent event){
        int xFrom, yFrom, xTo, yTo;
        int idFrom, idTo;
        idFrom = choiceFrom.getValue();
        idTo = choiceTo.getValue();
        xFrom = (int)circles.get(idFrom).getCenterX();
        yFrom = (int)circles.get(idFrom).getCenterY();
        xTo = (int)circles.get(idTo).getCenterX();
        yTo = (int)circles.get(idTo).getCenterY();

        idCurve curve = new idCurve();
        curve.setIdFrom(idFrom);
        curve.setIdTo(idTo);

        curve.setStartX(xFrom);
        curve.setStartY(yFrom);
        curve.setEndX(xTo);
        curve.setEndY(yTo);
        curve.setFill(Color.TRANSPARENT);
        curve.setStroke(Color.BLACK);
        curve.setStrokeWidth(1.5);
        curve.setViewOrder(pane.getChildren().size());
        curve.setControlX((xTo+xFrom)/2 + radius*(-1)*(yTo-yFrom + 0.0)/Math.sqrt(yTo*yTo + yFrom*yFrom));
        curve.setControlY((yTo+yFrom)/2 + radius*(+1)*(xTo-xFrom + 0.0)/Math.sqrt(xTo*xTo + xFrom*xFrom));

        curves.add(curve);
        pane.getChildren().add(curve);

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

        for (idCurve curve : curves){
            if (curve.idFrom == Integer.parseInt(((Circle)event.getSource()).getId())){
                curve.setStartX(x);
                curve.setStartY(y);
                curve.setControlX((curve.getEndX()+x)/2 + radius*(-1)*(curve.getEndY()-y + 0.0)/Math.sqrt(curve.getEndY()*curve.getEndY() + y*y));
                curve.setControlY((curve.getEndY()+y)/2 + radius*(+1)*(curve.getEndX()-x + 0.0)/Math.sqrt(curve.getEndX()*curve.getEndX() + x*x));
            }else if (curve.idTo == Integer.parseInt(((Circle)event.getSource()).getId())){
                curve.setEndX(x);
                curve.setEndY(y);
                curve.setControlX((x+curve.getStartX())/2 + radius*(-1)*(y-curve.getStartY() + 0.0)/Math.sqrt(y*y + curve.getStartY()*curve.getStartY()));
                curve.setControlY((y+curve.getStartY())/2 + radius*(+1)*(x-curve.getStartX() + 0.0)/Math.sqrt(x*x + curve.getStartX()*curve.getStartX()));
            }
        }
    }




}

class idCurve extends QuadCurve{
    int idTo;
    int idFrom;

    public int getIdTo() {
        return idTo;
    }

    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }

    public int getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }
}
