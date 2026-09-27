package com.japs.frontend.bodyfitgym.controllers.affiliatemembership;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.AffiliateMembership;
import com.japs.frontend.bodyfitgym.models.Membership;
import com.japs.frontend.bodyfitgym.models.SubscriptionStatus;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateMembershipService;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.services.MembershipService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import com.japs.frontend.bodyfitgym.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AffiliateMembershipListViewController implements Initializable {

    @FXML
    private TableColumn<AffiliateMembership, String> affiliateColumn;
    @FXML
    private TableColumn<AffiliateMembership, String> membershipColumn;
    @FXML
    private TableColumn<AffiliateMembership, String> startDateColumn;
    @FXML
    private TableColumn<AffiliateMembership, String> endDateColumn;
    @FXML
    private TableColumn<AffiliateMembership, String> remainingUnitsColumn;
    @FXML
    private TableColumn<AffiliateMembership, String> statusColumn;
    @FXML
    private TableColumn<AffiliateMembership, String> frozenColumn;
    @FXML
    private TableView<AffiliateMembership> affiliateMembershipTable;
    @FXML
    private TextField fieldSearch;
    @FXML
    private ComboBox<String> statusFilterCombo;
    @FXML
    private FontIcon iconSearch;
    @FXML
    private Pagination pagination;
    @FXML
    private Button registerButton;
    @FXML
    private MenuItem freezeMenuItem;
    @FXML
    private MenuItem unfreezeMenuItem;
    @FXML
    private MenuItem deleteMenuItem;

    private final ObservableList<AffiliateMembership> affiliateMembershipList = FXCollections.observableArrayList();
    private final AffiliateMembershipService affiliateMembershipService = new AffiliateMembershipService();
    private final AffiliateService affiliateService = new AffiliateService();
    private final MembershipService membershipService = new MembershipService();

    // Cache simple para no repetir llamadas find-by-id de un mismo afiliado/membresía
    // dentro de la misma carga de página (el backend no denormaliza nombres).
    private final Map<Long, String> affiliateNameCache = new HashMap<>();
    private final Map<Long, String> membershipNameCache = new HashMap<>();

    private Long currentAffiliateId = null;
    private SubscriptionStatus currentStatus = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable) ->
                listAffiliateMemberships(currentAffiliateId, currentStatus, pagination.getCurrentPageIndex()));
        configureColumns();
        configureRowFactory();
        applyRolePermissions();
        listAffiliateMemberships(null, null, 0);
    }

    private void configureColumns() {
        affiliateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                resolveAffiliateName(data.getValue().getAffiliateId())));
        membershipColumn.setCellValueFactory(data -> new SimpleStringProperty(
                resolveMembershipName(data.getValue().getMembershipId())));
        startDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStartDate() != null ? data.getValue().getStartDate().toString() : ""));
        endDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getEndDate() != null ? data.getValue().getEndDate().toString() : ""));
        remainingUnitsColumn.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getRemainingUnits())));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getStatus())));
        frozenColumn.setCellValueFactory(data -> new SimpleStringProperty(
                Boolean.TRUE.equals(data.getValue().getFrozen()) ? "Sí" : "No"));
    }

    private String resolveAffiliateName(Long affiliateId) {
        if (affiliateId == null) {
            return "";
        }
        return affiliateNameCache.computeIfAbsent(affiliateId, id -> {
            ServiceResponse<Affiliate> response = affiliateService.findById(id);
            if (response.getCode() == 200 && response.getData() != null) {
                Affiliate affiliate = response.getData();
                return affiliate.getIdentification() + " - " + affiliate.getFirstName() + " " + affiliate.getLastName();
            }
            return "ID " + id;
        });
    }

    private String resolveMembershipName(Long membershipId) {
        if (membershipId == null) {
            return "";
        }
        return membershipNameCache.computeIfAbsent(membershipId, id -> {
            ServiceResponse<Membership> response = membershipService.findById(id);
            if (response.getCode() == 200 && response.getData() != null) {
                return response.getData().getName();
            }
            return "ID " + id;
        });
    }

    private void configureRowFactory() {
        affiliateMembershipTable.setRowFactory(tv -> {
            TableRow<AffiliateMembership> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    affiliateMembershipTable.getSelectionModel().select(row.getIndex());
                }
            });
            return row;
        });
    }

    private void applyRolePermissions() {
        String role = Session.getCurrentUser() != null ? Session.getCurrentUser().getRole() : null;
        boolean canManage = "ADMINISTRADOR".equals(role) || "RECEPCIONISTA".equals(role);
        boolean canDelete = "ADMINISTRADOR".equals(role);

        registerButton.setVisible(canManage);
        registerButton.setManaged(canManage);
        freezeMenuItem.setVisible(canManage);
        freezeMenuItem.setDisable(!canManage);
        unfreezeMenuItem.setVisible(canManage);
        unfreezeMenuItem.setDisable(!canManage);
        deleteMenuItem.setVisible(canDelete);
        deleteMenuItem.setDisable(!canDelete);
    }

    private void listAffiliateMemberships(Long affiliateId, SubscriptionStatus status, int page) {
        affiliateMembershipList.clear();

        ServiceResponse<PageResponse<AffiliateMembership>> serviceResponse =
                affiliateMembershipService.search(affiliateId, null, status, page);

        if (serviceResponse.getCode() == 200) {
            PageResponse<AffiliateMembership> pageResponse = serviceResponse.getData();
            affiliateMembershipList.addAll(pageResponse.getContent());
            affiliateMembershipTable.setItems(affiliateMembershipList);
            pagination.setPageCount(Math.max(pageResponse.getTotalPages(), 1));
        } else {
            affiliateMembershipTable.setItems(affiliateMembershipList);
            pagination.setPageCount(1);
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo cargar el listado de suscripciones.");
        }
    }

    @FXML
    private void applyFilters(ActionEvent event) {
        String statusValue = statusFilterCombo.getValue();
        currentStatus = (statusValue == null || "Todas".equals(statusValue)) ? null : SubscriptionStatus.valueOf(statusValue);

        String identification = fieldSearch.getText();
        if (identification == null || identification.isBlank()) {
            currentAffiliateId = null;
            listAffiliateMemberships(null, currentStatus, 0);
            return;
        }

        ServiceResponse<PageResponse<Affiliate>> affiliateResponse = affiliateService.search(identification, null, null, 0);
        if (affiliateResponse.getCode() == 200 && !affiliateResponse.getData().getContent().isEmpty()) {
            currentAffiliateId = affiliateResponse.getData().getContent().get(0).getId();
            listAffiliateMemberships(currentAffiliateId, currentStatus, 0);
        } else {
            AlertUtils.showWarning("No se encontró ningún afiliado con esa identificación.");
            affiliateMembershipList.clear();
            affiliateMembershipTable.setItems(affiliateMembershipList);
            pagination.setPageCount(1);
        }
    }

    @FXML
    private void showAffiliateMembershipCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/affiliatemembership/AffiliateMembershipCreateView.fxml");
    }

    @FXML
    private void showAffiliateMembershipDetail(ActionEvent event) {
        AffiliateMembership selected = affiliateMembershipTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una suscripción para ver el detalle.");
            return;
        }

        ServiceResponse<Affiliate> affiliateResponse = affiliateService.findById(selected.getAffiliateId());
        ServiceResponse<Membership> membershipResponse = membershipService.findById(selected.getMembershipId());

        Affiliate affiliate = affiliateResponse.getCode() == 200 ? affiliateResponse.getData() : null;
        Membership membership = membershipResponse.getCode() == 200 ? membershipResponse.getData() : null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/affiliatemembership/AffiliateMembershipDetailView.fxml"));
            Parent root = loader.load();
            AffiliateMembershipDetailViewController controller = loader.getController();
            controller.setData(selected, affiliate, membership);

            Stage modalStage = new Stage();
            modalStage.setTitle("Detalle de la suscripción");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(affiliateMembershipTable.getScene().getWindow());
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showError("No se pudo abrir el detalle de la suscripción.");
        }
    }

    @FXML
    private void freezeAffiliateMembership(ActionEvent event) {
        AffiliateMembership selected = affiliateMembershipTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una suscripción para congelar.");
            return;
        }

        ServiceResponse<AffiliateMembership> response = affiliateMembershipService.freeze(selected.getId());
        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Suscripción congelada exitosamente.");
            listAffiliateMemberships(currentAffiliateId, currentStatus, pagination.getCurrentPageIndex());
        } else {
            AlertUtils.showError(response.getMessage() != null ? response.getMessage() : "No se pudo congelar la suscripción.");
        }
    }

    @FXML
    private void unfreezeAffiliateMembership(ActionEvent event) {
        AffiliateMembership selected = affiliateMembershipTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una suscripción para reanudar.");
            return;
        }

        ServiceResponse<AffiliateMembership> response = affiliateMembershipService.unfreeze(selected.getId());
        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Suscripción reanudada exitosamente.");
            listAffiliateMemberships(currentAffiliateId, currentStatus, pagination.getCurrentPageIndex());
        } else {
            AlertUtils.showError(response.getMessage() != null ? response.getMessage() : "No se pudo reanudar la suscripción.");
        }
    }

    @FXML
    private void deleteAffiliateMembership(ActionEvent event) {
        AffiliateMembership selected = affiliateMembershipTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una suscripción para eliminar.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("¿Desea eliminar esta suscripción?");
        if (!confirmed) {
            return;
        }

        ServiceResponse<Void> response = affiliateMembershipService.delete(selected.getId());
        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Suscripción eliminada con éxito.");
            listAffiliateMemberships(currentAffiliateId, currentStatus, pagination.getCurrentPageIndex());
        } else {
            AlertUtils.showError(response.getMessage() != null ? response.getMessage() : "No se pudo eliminar la suscripción.");
        }
    }
}
