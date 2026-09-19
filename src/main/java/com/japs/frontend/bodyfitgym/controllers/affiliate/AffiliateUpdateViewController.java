package com.japs.frontend.bodyfitgym.controllers.affiliate;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AffiliateUpdateViewController implements Initializable {

    @FXML private TextField identificationField;
    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField secondLastNameField;
    @FXML private ComboBox<String> sexCombo;
    @FXML private TextField birthDateField;
    @FXML private TextField occupationField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private TextField mobilePhoneField;
    @FXML private TextField homePhoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private TextField neighborhoodField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private TextField weightField;
    @FXML private TextField heightField;
    @FXML private TextField waistField;
    @FXML private TextField legField;
    @FXML private TextField hipField;

    private final AffiliateService affiliateService = new AffiliateService();
    private Affiliate affiliate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setAffiliate(Affiliate affiliate) {
        this.affiliate = affiliate;
        populateForm();
    }

    private void populateForm() {
        identificationField.setText(affiliate.getIdentification());
        firstNameField.setText(affiliate.getFirstName());
        middleNameField.setText(affiliate.getMiddleName());
        lastNameField.setText(affiliate.getLastName());
        secondLastNameField.setText(affiliate.getSecondLastName());
        sexCombo.setValue(affiliate.getSex());
        birthDateField.setText(affiliate.getBirthDate() != null ? affiliate.getBirthDate().toString() : "");
        occupationField.setText(affiliate.getOccupation());
        statusCombo.setValue(affiliate.getStatus());
        mobilePhoneField.setText(affiliate.getMobilePhone());
        homePhoneField.setText(affiliate.getHomePhone());
        emailField.setText(affiliate.getEmail());
        addressField.setText(affiliate.getAddress());
        neighborhoodField.setText(affiliate.getNeighborhood());
        cityField.setText(affiliate.getCity());
        postalCodeField.setText(affiliate.getPostalCode());
        weightField.setText(affiliate.getWeight() != null ? affiliate.getWeight().toString() : "");
        heightField.setText(affiliate.getHeight() != null ? affiliate.getHeight().toString() : "");
        waistField.setText(affiliate.getWaist() != null ? affiliate.getWaist().toString() : "");
        legField.setText(affiliate.getLeg() != null ? affiliate.getLeg().toString() : "");
        hipField.setText(affiliate.getHip() != null ? affiliate.getHip().toString() : "");
    }

    @FXML
    private void updateAffiliate(ActionEvent event) {
        if (identificationField.getText() == null || identificationField.getText().isBlank()) {
            AlertUtils.showWarning("La identificación es obligatoria.");
            return;
        }
        if (firstNameField.getText() == null || firstNameField.getText().isBlank()) {
            AlertUtils.showWarning("El primer nombre es obligatorio.");
            return;
        }
        if (lastNameField.getText() == null || lastNameField.getText().isBlank()) {
            AlertUtils.showWarning("El primer apellido es obligatorio.");
            return;
        }

        LocalDate birthDate = AffiliateFormMapper.parseDate(birthDateField.getText());
        if (birthDate == null && birthDateField.getText() != null && !birthDateField.getText().isBlank()) {
            AlertUtils.showWarning("La fecha de nacimiento debe tener el formato aaaa-mm-dd.");
            return;
        }

        Affiliate updated = new Affiliate();
        String error = AffiliateFormMapper.fill(updated, identificationField, firstNameField, middleNameField,
                lastNameField, secondLastNameField, sexCombo, birthDate, occupationField, mobilePhoneField,
                homePhoneField, emailField, addressField, neighborhoodField, cityField, postalCodeField,
                weightField, heightField, waistField, legField, hipField);
        if (error != null) {
            AlertUtils.showWarning(error);
            return;
        }
        updated.setStatus(statusCombo.getValue());

        ServiceResponse<Affiliate> serviceResponse = affiliateService.update(affiliate.getId(), updated);

        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Afiliado actualizado exitosamente.");
            showAffiliateListView(event);
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo actualizar el afiliado.");
        }
    }

    @FXML
    private void showAffiliateListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/affiliate/AffiliateListView.fxml");
    }
}
