package com.japs.frontend.bodyfitgym.controllers.affiliatemembership;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.AffiliateMembership;
import com.japs.frontend.bodyfitgym.models.Membership;
import com.japs.frontend.bodyfitgym.models.MembershipStatus;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateMembershipService;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.services.MembershipService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ResourceBundle;

public class AffiliateMembershipCreateViewController implements Initializable {

    @FXML
    private TextField identificationField;
    @FXML
    private Button searchButton;
    @FXML
    private Label affiliateFoundLabel;
    @FXML
    private ComboBox<Membership> membershipCombo;
    @FXML
    private TextField startDateField;
    @FXML
    private Button saveButton;

    private final AffiliateMembershipService affiliateMembershipService = new AffiliateMembershipService();
    private final AffiliateService affiliateService = new AffiliateService();
    private final MembershipService membershipService = new MembershipService();

    private Affiliate foundAffiliate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadActiveMemberships();
        membershipCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Membership membership) {
                return membership == null ? "" : membership.getName();
            }

            @Override
            public Membership fromString(String string) {
                return null;
            }
        });
    }

    private void loadActiveMemberships() {
        ServiceResponse<PageResponse<Membership>> response =
                membershipService.search(null, null, null, MembershipStatus.ACTIVA, 0);

        if (response.getCode() == 200) {
            membershipCombo.setItems(FXCollections.observableArrayList(response.getData().getContent()));
        } else {
            AlertUtils.showError("No se pudo cargar el catálogo de membresías activas.");
        }
    }

    @FXML
    private void searchAffiliate(ActionEvent event) {
        String identification = identificationField.getText();
        if (identification == null || identification.isBlank()) {
            AlertUtils.showWarning("Ingrese la identificación del afiliado a buscar.");
            return;
        }

        ServiceResponse<PageResponse<Affiliate>> response = affiliateService.search(identification, null, null, 0);
        if (response.getCode() == 200 && !response.getData().getContent().isEmpty()) {
            foundAffiliate = response.getData().getContent().get(0);
            affiliateFoundLabel.setText("Encontrado: " + foundAffiliate.getFirstName() + " " + foundAffiliate.getLastName());
        } else {
            foundAffiliate = null;
            affiliateFoundLabel.setText("");
            AlertUtils.showWarning("No se encontró ningún afiliado con esa identificación.");
        }
    }

    @FXML
    private void saveAffiliateMembership(ActionEvent event) {
        if (foundAffiliate == null) {
            AlertUtils.showWarning("Busque y confirme un afiliado antes de guardar.");
            return;
        }

        Membership selectedMembership = membershipCombo.getValue();
        if (selectedMembership == null) {
            AlertUtils.showWarning("Seleccione una membresía.");
            return;
        }

        LocalDate startDate = null;
        String startDateText = startDateField.getText();
        if (startDateText != null && !startDateText.isBlank()) {
            try {
                startDate = LocalDate.parse(startDateText.trim());
            } catch (DateTimeParseException e) {
                AlertUtils.showWarning("La fecha de inicio debe tener el formato aaaa-mm-dd.");
                return;
            }
        }

        AffiliateMembership affiliateMembership = new AffiliateMembership();
        affiliateMembership.setAffiliateId(foundAffiliate.getId());
        affiliateMembership.setMembershipId(selectedMembership.getId());
        affiliateMembership.setStartDate(startDate);

        ServiceResponse<AffiliateMembership> response = affiliateMembershipService.save(affiliateMembership);

        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Suscripción registrada exitosamente.");
            showAffiliateMembershipListView(event);
        } else {
            AlertUtils.showError(response.getMessage() != null
                    ? response.getMessage()
                    : "No se pudo registrar la suscripción.");
        }
    }

    @FXML
    private void showAffiliateMembershipListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/affiliatemembership/AffiliateMembershipListView.fxml");
    }
}
