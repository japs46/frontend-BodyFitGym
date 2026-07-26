/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.japs.frontend.bodyfitgym;

/**
 *
 * @author ASUS
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Frontend_BodyFitGym extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/templates/main/MainView.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.initStyle(StageStyle.DECORATED);
       primaryStage.initStyle(StageStyle.UNIFIED);
       // primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }


    public static void main(String[] args) {
        launch(args);
    }
}