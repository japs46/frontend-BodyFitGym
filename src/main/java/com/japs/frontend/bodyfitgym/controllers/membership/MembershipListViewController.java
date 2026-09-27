package com.japs.frontend.bodyfitgym.controllers.membership;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Membership;
import com.japs.frontend.bodyfitgym.models.MembershipStatus;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.MembershipService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import com.japs.frontend.bodyfitgym.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class MembershipListViewController implements Initializable {

    @FXML
    private TableColumn<Membership, String> nameColumn;
    @FXML
    private TableColumn<Membership, String> durationColumn;
    @FXML
    private TableColumn<Membership, String> trackingModeColumn;
    @FXML
    private TableColumn<Membership, String> priceColumn;
    @FXML
    private TableColumn<Membership, String> statusColumn;
    @FXML
    private TableView<Membership> membershipTable;
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
    private MenuItem updateMenuItem;
    @FXML
    private MenuItem deleteMenuItem;

    private final ObservableList<Membership> membershipList = FXCollections.observableArrayList();
    private final MembershipService membershipService = new MembershipService();

    private String currentName = null;
    private MembershipStatus currentStatus = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable) ->
                listMemberships(currentName, currentStatus, pagination.getCurrentPageIndex()));
        configureColumns();
        configureRowFactory();
        applyRolePermissions();
        listMemberships(null, null, 0);
    }

    private void configureColumns() {
        nameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("name"));
        durationColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDurationQuantity() + " " + data.getValue().getDurationUnit()));
        trackingModeColumn.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getTrackingMode())));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(
                "$ " + data.getValue().getPrice()));
        statusColumn.setCellValueFactory(data -> new SimpleStringProperty(
                String.valueOf(data.getValue().getStatus())));
    }

    private void configureRowFactory() {
        membershipTable.setRowFactory(tv -> {
            TableRow<Membership> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    membershipTable.getSelectionModel().select(row.getIndex());
                }
            });
            return row;
        });
    }

    private void applyRolePermissions() {
        String role = Session.getCurrentUser() != null ? Session.getCurrentUser().getRole() : null;
        boolean canCreate = "ADMINISTRADOR".equals(role) || "RECEPCIONISTA".equals(role);
        boolean canDelete = "ADMINISTRADOR".equals(role);

        registerButton.setVisible(canCreate);
        registerButton.setManaged(canCreate);
        updateMenuItem.setVisible(canDelete);
        updateMenuItem.setDisable(!canDelete);
        deleteMenuItem.setVisible(canDelete);
        deleteMenuItem.setDisable(!canDelete);
    }

    private void listMemberships(String name, MembershipStatus status, int page) {
        membershipList.clear();

        ServiceResponse<PageResponse<Membership>> serviceResponse = membershipService.search(name, null, null, status, page);

        if (serviceResponse.getCode() == 200) {
            PageResponse<Membership> pageResponse = serviceResponse.getData();
            membershipList.addAll(pageResponse.getContent());
            membershipTable.setItems(membershipList);
            pagination.setPageCount(Math.max(pageResponse.getTotalPages(), 1));
        } else {
            membershipTable.setItems(membershipList);
            pagination.setPageCount(1);
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo cargar el listado de membresías.");
        }
    }

    @FXML
    private void applyFilters(ActionEvent event) {
        currentName = fieldSearch.getText();
        String statusValue = statusFilterCombo.getValue();
        currentStatus = (statusValue == null || "Todas".equals(statusValue)) ? null : MembershipStatus.valueOf(statusValue);
        listMemberships(currentName, currentStatus, 0);
    }

    @FXML
    private void showMembershipCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/membership/MembershipCreateView.fxml");
    }

    @FXML
    private void showMembershipUpdateView(ActionEvent event) {
        Membership selected = membershipTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una membresía para actualizar.");
            return;
        }

        MainViewController.getInstance().cargarVistaMembershipUpdate(selected);
    }

    @FXML
    private void deleteMembership(ActionEvent event) {
        Membership selected = membershipTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una membresía para eliminar.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("¿Desea eliminar la membresía \"" + selected.getName() + "\"?");
        if (!confirmed) {
            return;
        }

        ServiceResponse<Void> serviceResponse = membershipService.delete(selected.getId());
        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Membresía eliminada con éxito.");
            listMemberships(currentName, currentStatus, pagination.getCurrentPageIndex());
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo eliminar la membresía.");
        }
    }
}
