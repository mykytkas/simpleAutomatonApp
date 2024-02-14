package com.example.demo1;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.text.Font;

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

    private FiniteAutomata automata;
    private ArrayList<Circle> circles = new ArrayList<>();
    private ArrayList<idCurve> curves = new ArrayList<>();
    private ArrayList<idLabel> labels = new ArrayList<>();
    private ArrayList<Label> circleLabels = new ArrayList<>();
    private ArrayList<Line> arrowheads = new ArrayList<>();
    private int oldX;
    private int oldY;
    final private int radius = 25;
    private int number;
    public void displayCircles(int number){
        this.number = number;
        oldX = (int)pane.getLayoutX();

        automata = new DeterministicFiniteAutomata();
        for (int i = 0; i < number; i++) {
            drawCircle(i);
            if(i == 0) automata.addVertex(true, false, String.valueOf(i));
            else automata.addVertex(false, true, String.valueOf(i));
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
        double xFrom, yFrom, xTo, yTo;
        int idFrom, idTo;
        idFrom = choiceFrom.getValue();
        idTo = choiceTo.getValue();
        xFrom = circles.get(idFrom).getCenterX();
        yFrom = circles.get(idFrom).getCenterY();
        xTo = circles.get(idTo).getCenterX();
        yTo = circles.get(idTo).getCenterY();
        xTo = arrowPointX(xFrom, xTo, yFrom, yTo);
        yTo = arrowPointY(xFrom, xTo, yFrom, yTo);
        //remove duplicates in accepted chars of transition
        String chars = removeDuplicates(charsField.getText());
        drawCurve(xFrom, xTo, yFrom, yTo, idFrom, idTo, chars);

        automata.getVertexById(idFrom).addEdge(idTo, chars);


    }

    @FXML
    protected void onMouseDragged(MouseEvent event) {
        //move circles
        int x = (int)event.getSceneX() - oldX;
        if (event.getSceneX() < pane.getPrefWidth() + pane.getLayoutX() - radius && event.getSceneX() > pane.getLayoutX() + radius){
            ((Circle)event.getSource()).setCenterX(x);

        }
        int y = (int)event.getSceneY() - oldY;
        if (event.getSceneY() < pane.getPrefHeight() - radius && event.getSceneY() > radius){
            ((Circle)event.getSource()).setCenterY(y);

        }
        //move attached curves
        for (idCurve curve : curves){
            if (curve.getIdFrom() == Integer.parseInt(((Circle)event.getSource()).getId())){
                curve.setStartX(x);
                curve.setStartY(y);

                curve.setEndX(arrowPointX(x, circles.get(curve.getIdTo()).getCenterX(),
                        y, circles.get(curve.getIdTo()).getCenterY() ));
                curve.setEndY(arrowPointY(x, circles.get(curve.getIdTo()).getCenterX(),
                        y, circles.get(curve.getIdTo()).getCenterY() ));

                curve.setControlX(controlPointX(x, curve.getEndX(), y, curve.getEndY(),1,+1));
                curve.setControlY(controlPointY(x, curve.getEndX(), y, curve.getEndY(),1,-1));

                eraseArrowHead(curve.getId());
            }else if (curve.getIdTo() == Integer.parseInt(((Circle)event.getSource()).getId())){
                curve.setEndX(arrowPointX(circles.get(curve.getIdFrom()).getCenterX(), x,
                       circles.get(curve.getIdFrom()).getCenterY(), y ));
                curve.setEndY(arrowPointY(circles.get(curve.getIdFrom()).getCenterX(), x,
                        circles.get(curve.getIdFrom()).getCenterY(), y ));

                curve.setControlX(controlPointX(curve.getStartX(), x, curve.getStartY(), y, 1, +1));
                curve.setControlY(controlPointY(curve.getStartX(), x, curve.getStartY(), y, 1, -1));

                eraseArrowHead(curve.getId());
            }
        }
        //move curve - label
        for (idLabel label : labels){
            if (label.getIdFrom() == Integer.parseInt(((Circle)event.getSource()).getId())){
                double xTo = circles.get(label.getIdTo()).getCenterX();
                double yTo = circles.get(label.getIdTo()).getCenterY();
                label.setLayoutX(controlPointX(x, xTo, y, yTo, 1.5, +1));
                label.setLayoutY(controlPointY(x, xTo, y, yTo, 1.5, -1));
            }else if (label.getIdTo() == Integer.parseInt(((Circle)event.getSource()).getId())){
                double xFrom = circles.get(label.getIdFrom()).getCenterX();
                double yFrom = circles.get(label.getIdFrom()).getCenterY();
                label.setLayoutX(controlPointX(xFrom, x, yFrom, y, 1.5, +1));
                label.setLayoutY(controlPointY(xFrom, x, yFrom, y, 1.5, -1));
            }
        }
        //move circle label
        for (Label label : circleLabels){
            if (label.getId().equals(((Circle)event.getSource()).getId())){
                label.setLayoutX(x-radius/5);
                label.setLayoutY(y-radius/2);
            }

        }

    }
    @FXML
    protected void onMouseReleased(MouseEvent event){
        for (idCurve curve : curves){
            if (curve.getIdTo() == Integer.parseInt(((Circle)event.getSource()).getId()) || curve.getIdFrom() == Integer.parseInt(((Circle)event.getSource()).getId())){
                double xFrom, yFrom, xTo, yTo;

                xFrom = circles.get(curve.getIdFrom()).getCenterX();
                yFrom = circles.get(curve.getIdFrom()).getCenterY();
                xTo = circles.get(curve.getIdTo()).getCenterX();
                yTo = circles.get(curve.getIdTo()).getCenterY();
                xTo = arrowPointX(xFrom, xTo, yFrom, yTo);
                yTo = arrowPointY(xFrom, xTo, yFrom, yTo);

                drawArrowHead(xFrom, xTo, yFrom, yTo, curve.getId());
            }
        }
    }

    @FXML
    protected void checkString(ActionEvent event){
        String input = stringField.getText();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (automata.isAcceptableString(input)){
            alert.setHeaderText("could be accepted");
        }else{
            alert.setHeaderText("could not be accepted");
        }
        alert.show();
    }
    private void drawCircle(int i){
        Circle circle = new Circle();
        double x = 150*Math.cos((i+1)*2*Math.PI/number) + pane.getPrefWidth()/2;
        double y = 150*Math.sin((i+1)*2*Math.PI/number) + pane.getPrefHeight()/2;
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
        circle.setId(String.valueOf(i));
        circle.setFill(Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(2);
        circle.setOnMouseReleased(this::onMouseReleased);
        circle.setOnMouseDragged(this::onMouseDragged);
        circles.add(circle);
        pane.getChildren().add(circle);

        Label label = new Label();
        label.setText(String.valueOf(i));
        label.setFont(Font.font(20));
        label.setLayoutX(x-radius/5);
        label.setLayoutY(y-radius/2);
        label.setId(String.valueOf(i));
        label.setDisable(true);
        circleLabels.add(label);
        pane.getChildren().add(label);
    }
    private void drawCurve(double xFrom, double xTo,double yFrom, double yTo, int idFrom, int idTo, String chars){
        //add idCurve
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
        curve.setControlX(controlPointX(xFrom, xTo, yFrom, yTo, 1, +1));
        curve.setControlY(controlPointY(xFrom, xTo, yFrom, yTo, 1, -1));
        curve.setId(String.valueOf(curves.size()));

        curves.add(curve);
        pane.getChildren().add(curve);

        drawArrowHead(xFrom, xTo, yFrom, yTo, curve.getId());
        //add label
        idLabel label = new idLabel();
        label.setIdFrom(idFrom);
        label.setIdTo(idTo);
        label.setText(chars);
        label.setLayoutX(controlPointX(xFrom, xTo, yFrom, yTo, 1.5, +1));
        label.setLayoutY(controlPointY(xFrom, xTo, yFrom, yTo, 1.5, -1));
        labels.add(label);
        pane.getChildren().add(label);
    }
    private void drawArrowHead(double xFrom, double xTo, double yFrom, double yTo, String curveId){
        double factorDelta = (20)/Math.hypot(xTo - xFrom, yTo - yFrom);

        double dx = (xTo - xFrom) * factorDelta;
        double dy = (yTo - yFrom) * factorDelta;

        double ox = dx / 2;
        double oy = dy / 2;

        Line line1 = new Line();
        line1.setStartX(xTo);
        line1.setStartY(yTo);
        line1.setEndX(xTo + oy - dx);
        line1.setEndY(yTo - ox - dy);
        line1.setStrokeWidth(1.5);
        line1.setId(curveId);
        arrowheads.add(line1);
        pane.getChildren().add(line1);

        Line line2 = new Line();
        line2.setStartX(xTo);
        line2.setStartY(yTo);
        line2.setEndX(xTo - oy - dx);
        line2.setEndY(yTo + ox - dy);
        line1.setStrokeWidth(1.5);
        line2.setId(curveId);
        arrowheads.add(line2);
        pane.getChildren().add(line2);

    }
    private void eraseArrowHead(String curveId){
        for (int i = 0; i < arrowheads.size(); i++){
            if (arrowheads.get(i).getId().equals(curveId)){
                pane.getChildren().remove(arrowheads.get(i));
                arrowheads.remove(arrowheads.get(i));
            }
        }

    }
    //control point Methods are the same operation, they are separated for the sake of comfort
    private double controlPointX(double xFrom, double xTo, double yFrom, double yTo, double labelFactor, int sign){
        return (xTo+xFrom)/2 + radius*labelFactor*sign*(yTo-yFrom)/Math.sqrt(yTo*yTo + yFrom*yFrom);
    }
    private double controlPointY(double xFrom, double xTo, double yFrom, double yTo, double labelFactor, int sign){
        return (yTo+yFrom)/2 + radius*labelFactor*sign*(xTo-xFrom)/Math.sqrt(xTo*xTo + xFrom*xFrom);
    }
    private double arrowPointX(double xFrom, double xTo, double yFrom, double yTo){
        return xTo - radius*(xTo-xFrom)/Math.sqrt((xTo - xFrom)*(xTo - xFrom) + (yTo - yFrom)*(yTo - yFrom));
    }
    private double arrowPointY(double xFrom, double xTo, double yFrom, double yTo){
        return yTo - radius*(yTo-yFrom)/Math.sqrt((xTo - xFrom)*(xTo - xFrom) + (yTo - yFrom)*(yTo - yFrom));
    }
    private String removeDuplicates(String input){
        String out = "";
        for (char c : input.toCharArray()){
            boolean shouldAdd = true;
            for (char o : out.toCharArray()){
                if(c == o) shouldAdd = false;
            }
            if(shouldAdd) out = out + c;
        }
        return out;
    }

}

class idCurve extends QuadCurve{
    private int idTo;
    private int idFrom;

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
class idLabel extends Label{
    private int idFrom;
    private int idTo;

    public int getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(int idFrom) {
        this.idFrom = idFrom;
    }

    public int getIdTo() {
        return idTo;
    }

    public void setIdTo(int idTo) {
        this.idTo = idTo;
    }
}
