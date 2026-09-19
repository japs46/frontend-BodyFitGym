package com.japs.frontend.bodyfitgym.controllers.membership;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.DurationUnit;
import com.japs.frontend.bodyfitgym.models.Membership;
import com.japs.frontend.bodyfitgym.models.MembershipStatus;
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

public class MembershipUpdateViewController implements Initializable {

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
    @FXML
    private ComboBox<String> statusCombo;

    private final MembershipService membershipService = new MembershipService();
    private Membership membership;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
        populateForm();
    }

    private void populateForm() {
        nameField.setText(membership.getName());
        descriptionField.setText(membership.getDescription());
        priceField.setText(membership.getPrice() != null ? membership.getPrice().toString() : "");
        durationQuantityField.setText(membership.getDurationQuantity() != null ? String.valueOf(membership.getDurationQuantity()) : "");
        durationUnitCombo.setValue(membership.getDurationUnit() != null ? membership.getDurationUnit().name() : null);
        trackingModeCombo.setValue(membership.getTrackingMode() != null ? membership.getTrackingMode().name() : null);
        statusCombo.setValue(membership.getStatus() != null ? membership.getStatus().name() : null);
    }

    @FXML
    private void updateMembership(ActionEvent event) {
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

        if (statusCombo.getValue() == null) {
            AlertUtils.showWarning("Seleccione el estado de la membresía.");
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

        Membership updated = new Membership();
        updated.setName(nameField.getText().trim());
        updated.setDescription(descriptionField.getText() == null ? null : descriptionField.getText().trim());
        updated.setPrice(price);
        updated.setDurationQuantity(durationQuantity);
        updated.setDurationUnit(DurationUnit.valueOf(durationUnitCombo.getValue()));
        updated.setTrackingMode(TrackingMode.valueOf(trackingModeCombo.getValue()));
        updated.setStatus(MembershipStatus.valueOf(statusCombo.getValue()));

        ServiceResponse<Membership> serviceResponse = membershipService.update(membership.getId(), updated);

        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Membresía actualizada exitosamente.");
            showMembershipListView(event);
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo actualizar la membresía.");
        }
    }

    @FXML
    private void showMembershipListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/membership/MembershipListView.fxml");
    }
}
