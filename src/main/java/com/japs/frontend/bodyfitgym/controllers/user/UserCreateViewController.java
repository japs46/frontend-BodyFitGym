package com.japs.frontend.bodyfitgym.controllers.user;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class UserCreateViewController implements Initializable {



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void showUserListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/user/UserListView.fxml");
    }
}
