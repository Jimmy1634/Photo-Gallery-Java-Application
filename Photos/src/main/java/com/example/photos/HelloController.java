package com.example.photos;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

public class HelloController {
    @FXML
    private Circle Sun;

    private double y;

    public void rise(){
        Sun.setCenterY(y-=40);

    }
}