package com.japs.frontend.bodyfitgym.controllers.membership;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.DurationUnit;
import com.japs.frontend.bodyfitgym.models.Membership;
import com.japs.frontend.bodyfitgym.models.TrackingMode;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.MembershipService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class MembershipCreateViewController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField durationQuantityField;
    @FXML
    private ComboBox<String> durationUnitCombo;
    @FXML
    private ComboBox<String> trackingModeCombo;

    private final MembershipService membershipService = new MembershipService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void saveMembership(ActionEvent event) {
        if (nameField.getText() == null || nameField.getText().isBlank()) {
            AlertUtils.showWarning("El nombre de la membresía es obligatorio.");
            return;
        }

        if (durationUnitCombo.getValue() == null) {
            AlertUtils.showWarning("Seleccione la unidad de duración.");
            return;
        }

        if (trackingModeCombo.getValue() == null) {
            AlertUtils.showWarning("Seleccione el modo de seguimiento.");
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceField.getText().trim());
        } catch (Exception e) {
            AlertUtils.showWarning("El precio debe ser un número válido.");
            return;
        }

        Integer durationQuantity;
        try {
            durationQuantity = Integer.parseInt(durationQuantityField.getText().trim());
        } catch (Exception e) {
            AlertUtils.showWarning("La cantidad de duración debe ser un número entero válido.");
            return;
        }

        Membership membership = new Membership();
        membership.setName(nameField.getText().trim());
        membership.setDescription(descriptionField.getText() == null ? null : descriptionField.getText().trim());
        membership.setPrice(price);
        membership.setDurationQuantity(durationQuantity);
        membership.setDurationUnit(DurationUnit.valueOf(durationUnitCombo.getValue()));
        membership.setTrackingMode(TrackingMode.valueOf(trackingModeCombo.getValue()));

        ServiceResponse<Membership> serviceResponse = membershipService.save(membership);

        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Membresía registrada exitosamente.");
            showMembershipListView(event);
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo registrar la membresía.");
        }
    }

    @FXML
    private void cleanForm(ActionEvent event) {
        nameField.clear();
        descriptionField.clear();
        priceField.clear();
        durationQuantityField.clear();
        durationUnitCombo.setValue(null);
        trackingModeCombo.setValue(null);
    }

    @FXML
    private void showMembershipListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/membership/MembershipListView.fxml");
    }
}
