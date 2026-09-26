package com.japs.frontend.bodyfitgym.controllers.affiliate;

import com.japs.frontend.bodyfitgym.models.Affiliate;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AffiliateDetailViewController implements Initializable {

    @FXML private Label identificationValue;
    @FXML private Label fullNameValue;
    @FXML private Label sexValue;
    @FXML private Label birthDateValue;
    @FXML private Label affiliationDateValue;
    @FXML private Label occupationValue;
    @FXML private Label statusValue;
    @FXML private Label mobilePhoneValue;
    @FXML private Label homePhoneValue;
    @FXML private Label emailValue;
    @FXML private Label addressValue;
    @FXML private Label neighborhoodValue;
    @FXML private Label cityValue;
    @FXML private Label postalCodeValue;
    @FXML private Label weightValue;
    @FXML private Label heightValue;
    @FXML private Label waistValue;
    @FXML private Label legValue;
    @FXML private Label hipValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setAffiliate(Affiliate affiliate) {
        identificationValue.setText(orDash(affiliate.getIdentification()));
        fullNameValue.setText(orDash(fullName(affiliate)));
        sexValue.setText(orDash(affiliate.getSex()));
        birthDateValue.setText(affiliate.getBirthDate() != null ? affiliate.getBirthDate().toString() : "-");
        affiliationDateValue.setText(affiliate.getAffiliationDate() != null ? affiliate.getAffiliationDate().toString() : "-");
        occupationValue.setText(orDash(affiliate.getOccupation()));
        statusValue.setText(orDash(affiliate.getStatus()));

        mobilePhoneValue.setText(orDash(affiliate.getMobilePhone()));
        homePhoneValue.setText(orDash(affiliate.getHomePhone()));
        emailValue.setText(orDash(affiliate.getEmail()));
        addressValue.setText(orDash(affiliate.getAddress()));
        neighborhoodValue.setText(orDash(affiliate.getNeighborhood()));
        cityValue.setText(orDash(affiliate.getCity()));
        postalCodeValue.setText(orDash(affiliate.getPostalCode()));

        weightValue.setText(affiliate.getWeight() != null ? affiliate.getWeight().toString() : "-");
        heightValue.setText(affiliate.getHeight() != null ? affiliate.getHeight().toString() : "-");
        waistValue.setText(affiliate.getWaist() != null ? affiliate.getWaist().toString() : "-");
        legValue.setText(affiliate.getLeg() != null ? affiliate.getLeg().toString() : "-");
        hipValue.setText(affiliate.getHip() != null ? affiliate.getHip().toString() : "-");
    }

    private String fullName(Affiliate a) {
        StringBuilder sb = new StringBuilder();
        if (a.getFirstName() != null) sb.append(a.getFirstName());
        if (a.getMiddleName() != null && !a.getMiddleName().isBlank()) sb.append(" ").append(a.getMiddleName());
        if (a.getLastName() != null) sb.append(" ").append(a.getLastName());
        if (a.getSecondLastName() != null && !a.getSecondLastName().isBlank()) sb.append(" ").append(a.getSecondLastName());
        return sb.toString().trim();
    }

    private String orDash(String value) {
        return (value == null || value.isBlank()) ? "-" : value;
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
