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

public class AffiliateCreateViewController implements Initializable {

    @FXML private TextField identificationField;
    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField secondLastNameField;
    @FXML private ComboBox<String> sexCombo;
    @FXML private TextField birthDateField;
    @FXML private TextField occupationField;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void saveAffiliate(ActionEvent event) {
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

        Affiliate affiliate = new Affiliate();
        String error = AffiliateFormMapper.fill(affiliate, identificationField, firstNameField, middleNameField,
                lastNameField, secondLastNameField, sexCombo, birthDate, occupationField, mobilePhoneField,
                homePhoneField, emailField, addressField, neighborhoodField, cityField, postalCodeField,
                weightField, heightField, waistField, legField, hipField);
        if (error != null) {
            AlertUtils.showWarning(error);
            return;
        }

        ServiceResponse<Affiliate> serviceResponse = affiliateService.save(affiliate);

        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Afiliado registrado exitosamente.");
            showAffiliateListView(event);
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo registrar el afiliado.");
        }
    }

    @FXML
    private void cleanForm(ActionEvent event) {
        identificationField.clear();
        firstNameField.clear();
        middleNameField.clear();
        lastNameField.clear();
        secondLastNameField.clear();
        sexCombo.setValue(null);
        birthDateField.clear();
        occupationField.clear();
        mobilePhoneField.clear();
        homePhoneField.clear();
        emailField.clear();
        addressField.clear();
        neighborhoodField.clear();
        cityField.clear();
        postalCodeField.clear();
        weightField.clear();
        heightField.clear();
        waistField.clear();
        legField.clear();
        hipField.clear();
    }

    @FXML
    private void showAffiliateListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/affiliate/AffiliateListView.fxml");
    }
}
