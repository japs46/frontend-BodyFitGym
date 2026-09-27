/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.japs.frontend.bodyfitgym.controllers.main;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.japs.frontend.bodyfitgym.controllers.affiliate.AffiliateUpdateViewController;
import com.japs.frontend.bodyfitgym.controllers.membership.MembershipUpdateViewController;
import com.japs.frontend.bodyfitgym.controllers.product.ProductUpdateViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.Membership;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.utils.Session;

/**
 * FXML Controller class
 *
 * @author carde
 */
public class MainViewController implements Initializable {

    private static MainViewController instance;

    public static MainViewController getInstance(){
        return instance;
    }

    @FXML
    private StackPane stakPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        instance = this;
    }


    public void cargarVista(String fxml) {
        try {
            Parent vista = FXMLLoader.load(getClass().getResource(fxml));
            stakPane.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarVistaMembershipUpdate(Membership membership) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/membership/MembershipUpdateView.fxml"));
            Parent vista = loader.load();
            MembershipUpdateViewController controller = loader.getController();
            controller.setMembership(membership);
            stakPane.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarVistaAffiliateUpdate(Affiliate affiliate) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/affiliate/AffiliateUpdateView.fxml"));
            Parent vista = loader.load();
            AffiliateUpdateViewController controller = loader.getController();
            controller.setAffiliate(affiliate);
            stakPane.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cargarVistaProductUpdate(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/product/ProductUpdateView.fxml"));
            Parent vista = loader.load();
            ProductUpdateViewController controller = loader.getController();
            controller.setProduct(product);
            stakPane.getChildren().setAll(vista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void logOut(MouseEvent event) {
        try {
            Session.clear();
            Parent root = FXMLLoader.load(getClass().getResource("/templates/main/LoginView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            stage.centerOnScreen();
        } catch (IOException e) {
            System.out.println("Error al Cargar Login: " + e.getMessage());
        }

    }

    @FXML
    private void cambiarVistaUsuario(MouseEvent event) {
        cargarVista("/templates/user/UserListView.fxml");
    }

    @FXML
    private void cambiarVistaMembresia(MouseEvent event) {
        cargarVista("/templates/membership/MembershipListView.fxml");
    }

    @FXML
    private void cambiarVistaAfiliado(MouseEvent event) {
        cargarVista("/templates/affiliate/AffiliateListView.fxml");
    }

    @FXML
    private void cambiarVistaAfiliacion(MouseEvent event) {
        cargarVista("/templates/affiliatemembership/AffiliateMembershipListView.fxml");
    }

    @FXML
    private void cambiarVistaAsistencia(MouseEvent event) {
        cargarVista("/templates/attendance/AttendanceListView.fxml");
    }

    @FXML
    private void cambiarVistaProducto(MouseEvent event) {
        cargarVista("/templates/product/ProductListView.fxml");
    }

    @FXML
    private void cambiarVistaVenta(MouseEvent event) {
        cargarVista("/templates/sale/SaleListView.fxml");
    }
}
