/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.japs.frontend.bodyfitgym.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import com.japs.frontend.bodyfitgym.models.AuthResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AuthService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import com.japs.frontend.bodyfitgym.utils.Session;

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

    private final AuthService authService = new AuthService();

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

        if (userName == null || userName.isBlank() || password == null || password.isBlank()) {
            AlertUtils.showWarning("Debe ingresar el usuario y la contraseña.");
            return;
        }

        ServiceResponse<AuthResponse> serviceResponse = authService.login(userName, password);

        if (serviceResponse.isStatus() && serviceResponse.getData() != null) {
            AuthResponse user = serviceResponse.getData();
            Session.start(user);
            AlertUtils.showSuccess("Bienvenido " + user.getName() + " (" + user.getRole() + ")");
            openMainView(event);
        } else {
            String errorMessage = serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No fue posible iniciar sesión. Intente nuevamente.";
            AlertUtils.showError(errorMessage);
        }
    }

    private void openMainView(ActionEvent event) {
        try {
            Parent mainView = FXMLLoader.load(getClass().getResource("/templates/main/MainView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainView));
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            AlertUtils.showError("No fue posible cargar la aplicación: " + e.getMessage());
        }
    }
}
