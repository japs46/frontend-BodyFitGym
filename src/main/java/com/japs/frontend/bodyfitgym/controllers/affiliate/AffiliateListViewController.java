package com.japs.frontend.bodyfitgym.controllers.affiliate;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AffiliateListViewController implements Initializable {

    @FXML
    private TableColumn<Affiliate, String> identificationColumn;
    @FXML
    private TableColumn<Affiliate, String> nameColumn;
    @FXML
    private TableColumn<Affiliate, String> mobilePhoneColumn;
    @FXML
    private TableColumn<Affiliate, String> cityColumn;
    @FXML
    private TableColumn<Affiliate, String> statusColumn;
    @FXML
    private TableView<Affiliate> affiliateTable;
    @FXML
    private TextField fieldSearch;
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

    private final ObservableList<Affiliate> affiliateList = FXCollections.observableArrayList();
    private final AffiliateService affiliateService = new AffiliateService();

    private String currentIdentification = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable) ->
                listAffiliates(currentIdentification, pagination.getCurrentPageIndex()));
        configureColumns();
        configureRowFactory();
        applyRolePermissions();
        listAffiliates(null, 0);
    }

    private void configureColumns() {
        identificationColumn.setCellValueFactory(new PropertyValueFactory<>("identification"));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(fullName(data.getValue())));
        mobilePhoneColumn.setCellValueFactory(new PropertyValueFactory<>("mobilePhone"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private String fullName(Affiliate a) {
        StringBuilder sb = new StringBuilder();
        if (a.getFirstName() != null) sb.append(a.getFirstName());
        if (a.getMiddleName() != null && !a.getMiddleName().isBlank()) sb.append(" ").append(a.getMiddleName());
        if (a.getLastName() != null) sb.append(" ").append(a.getLastName());
        if (a.getSecondLastName() != null && !a.getSecondLastName().isBlank()) sb.append(" ").append(a.getSecondLastName());
        return sb.toString().trim();
    }

    private void configureRowFactory() {
        affiliateTable.setRowFactory(tv -> {
            TableRow<Affiliate> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    affiliateTable.getSelectionModel().select(row.getIndex());
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
        updateMenuItem.setVisible(canManage);
        updateMenuItem.setDisable(!canManage);
        deleteMenuItem.setVisible(canDelete);
        deleteMenuItem.setDisable(!canDelete);
    }

    private void listAffiliates(String identification, int page) {
        affiliateList.clear();

        ServiceResponse<PageResponse<Affiliate>> serviceResponse = affiliateService.search(identification, null, null, page);

        if (serviceResponse.getCode() == 200) {
            PageResponse<Affiliate> pageResponse = serviceResponse.getData();
            affiliateList.addAll(pageResponse.getContent());
            affiliateTable.setItems(affiliateList);
            pagination.setPageCount(Math.max(pageResponse.getTotalPages(), 1));
        } else {
            affiliateTable.setItems(affiliateList);
            pagination.setPageCount(1);
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo cargar el listado de afiliados.");
        }
    }

    @FXML
    private void searchAffiliate(ActionEvent event) {
        currentIdentification = fieldSearch.getText();
        listAffiliates(currentIdentification, 0);
    }

    @FXML
    private void showAffiliateCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/affiliate/AffiliateCreateView.fxml");
    }

    @FXML
    private void showAffiliateDetail(ActionEvent event) {
        Affiliate selected = affiliateTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione un afiliado para ver el detalle.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/templates/affiliate/AffiliateDetailView.fxml"));
            Parent root = loader.load();
            AffiliateDetailViewController controller = loader.getController();
            controller.setAffiliate(selected);

            Stage modalStage = new Stage();
            modalStage.setTitle("Detalle del afiliado");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(affiliateTable.getScene().getWindow());
            modalStage.setScene(new Scene(root));
            modalStage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showError("No se pudo abrir el detalle del afiliado.");
        }
    }

    @FXML
    private void showAffiliateUpdateView(ActionEvent event) {
        Affiliate selected = affiliateTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione un afiliado para actualizar.");
            return;
        }

        MainViewController.getInstance().cargarVistaAffiliateUpdate(selected);
    }

    @FXML
    private void deleteAffiliate(ActionEvent event) {
        Affiliate selected = affiliateTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione un afiliado para eliminar.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("¿Desea eliminar al afiliado \"" + fullName(selected) + "\"?");
        if (!confirmed) {
            return;
        }

        ServiceResponse<Void> serviceResponse = affiliateService.delete(selected.getId());
        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Afiliado eliminado con éxito.");
            listAffiliates(currentIdentification, pagination.getCurrentPageIndex());
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo eliminar el afiliado.");
        }
    }
}
