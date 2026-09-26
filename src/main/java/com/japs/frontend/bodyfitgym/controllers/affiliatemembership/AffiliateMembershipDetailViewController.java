package com.japs.frontend.bodyfitgym.controllers.affiliatemembership;

import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.AffiliateMembership;
import com.japs.frontend.bodyfitgym.models.Membership;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AffiliateMembershipDetailViewController implements Initializable {

    @FXML private Label statusValue;
    @FXML private Label frozenValue;
    @FXML private Label trackingModeValue;
    @FXML private Label remainingUnitsValue;
    @FXML private Label startDateValue;
    @FXML private Label endDateValue;
    @FXML private Label frozenAtValue;
    @FXML private Label createdAtValue;

    @FXML private Label affiliateIdentificationValue;
    @FXML private Label affiliateNameValue;
    @FXML private Label affiliateMobilePhoneValue;
    @FXML private Label affiliateEmailValue;
    @FXML private Label affiliateCityValue;
    @FXML private Label affiliateStatusValue;

    @FXML private Label membershipNameValue;
    @FXML private Label membershipDescriptionValue;
    @FXML private Label membershipPriceValue;
    @FXML private Label membershipDurationValue;
    @FXML private Label membershipTrackingModeValue;
    @FXML private Label membershipStatusValue;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setData(AffiliateMembership subscription, Affiliate affiliate, Membership membership) {
        statusValue.setText(orDash(subscription.getStatus() != null ? subscription.getStatus().toString() : null));
        frozenValue.setText(Boolean.TRUE.equals(subscription.getFrozen()) ? "Sí" : "No");
        trackingModeValue.setText(orDash(subscription.getTrackingMode() != null ? subscription.getTrackingMode().toString() : null));
        remainingUnitsValue.setText(subscription.getRemainingUnits() != null ? subscription.getRemainingUnits().toString() : "-");
        startDateValue.setText(subscription.getStartDate() != null ? subscription.getStartDate().toString() : "-");
        endDateValue.setText(subscription.getEndDate() != null ? subscription.getEndDate().toString() : "-");
        frozenAtValue.setText(subscription.getFrozenAt() != null ? subscription.getFrozenAt().toString() : "-");
        createdAtValue.setText(subscription.getCreatedAt() != null ? subscription.getCreatedAt().toString() : "-");

        if (affiliate != null) {
            affiliateIdentificationValue.setText(orDash(affiliate.getIdentification()));
            affiliateNameValue.setText(orDash(affiliate.getFirstName() + " " + affiliate.getLastName()));
            affiliateMobilePhoneValue.setText(orDash(affiliate.getMobilePhone()));
            affiliateEmailValue.setText(orDash(affiliate.getEmail()));
            affiliateCityValue.setText(orDash(affiliate.getCity()));
            affiliateStatusValue.setText(orDash(affiliate.getStatus()));
        }

        if (membership != null) {
            membershipNameValue.setText(orDash(membership.getName()));
            membershipDescriptionValue.setText(orDash(membership.getDescription()));
            membershipPriceValue.setText(membership.getPrice() != null ? "$ " + membership.getPrice() : "-");
            membershipDurationValue.setText(membership.getDurationQuantity() != null
                    ? membership.getDurationQuantity() + " " + membership.getDurationUnit()
                    : "-");
            membershipTrackingModeValue.setText(orDash(membership.getTrackingMode() != null ? membership.getTrackingMode().toString() : null));
            membershipStatusValue.setText(orDash(membership.getStatus() != null ? membership.getStatus().toString() : null));
        }
    }

    private String orDash(String value) {
        return (value == null || value.isBlank()) ? "-" : value;
    }

    @FXML
    private void close(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
