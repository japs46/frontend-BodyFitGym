/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.japs.frontend.bodyfitgym.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author carde
 */
public class LoginViewController implements Initializable {


    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ImageView btnClose;
    @FXML
    private AnchorPane root;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> root.requestFocus());
    }

    @FXML
    private void closeLogin(MouseEvent event) {
        Platform.exit();
    }

    @FXML
    private void authenticateUser(ActionEvent event) {
        String userName = txtUser.getText();
        String password = txtPassword.getText();


    }
    public void message(String tittle,String contentText){
        Alert alert = new Alert (Alert.AlertType.ERROR) ;
        alert.setHeaderText(null) ;
        alert.setTitle(tittle);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
