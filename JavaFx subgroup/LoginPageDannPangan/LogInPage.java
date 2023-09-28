package com.example.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Pane root  = new Pane();

        Circle c0 = new Circle(200);
        c0.setFill(Color.YELLOW);
        c0.setCenterX(250);
        c0.setCenterY(250);
        c0.setStroke(Color.BLACK);
        c0.setStrokeWidth(5);

        Circle c1 = new Circle(40);
        c1.setFill(Color.BLUE);
        c1.setStroke(Color.BLACK);
        c1.setStrokeWidth(5);

        Circle c2 = new Circle(40);
        c2.setFill(Color.GREEN);
        c2.setStroke(Color.BLACK);
        c2.setStrokeWidth(5);

        Circle c3 = new Circle(75);
        c3.setFill(Color.TRANSPARENT);
        c3.setStroke(Color.BLACK);
        c3.setStrokeWidth(5);

        Rectangle r1 = new Rectangle(200,100);
        r1.setFill(Color.YELLOW);
        r1.setStroke(Color.YELLOW);

        c1.setCenterX(150);
        c1.setCenterY(150);

        c2.setCenterX(350);
        c2.setCenterY(150);

        c3.setCenterX(250);
        c3.setCenterY(300);

        r1.setX(150);
        r1.setY(200);


        root.getChildren().addAll(c0,c1,c2,c3,r1);
        Scene scene = new Scene(root, 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}