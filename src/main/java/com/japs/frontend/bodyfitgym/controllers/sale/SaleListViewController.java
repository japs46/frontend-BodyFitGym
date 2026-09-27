package com.japs.frontend.bodyfitgym.controllers.sale;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.models.Sale;
import com.japs.frontend.bodyfitgym.models.SaleType;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.services.ProductService;
import com.japs.frontend.bodyfitgym.services.SaleService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import com.japs.frontend.bodyfitgym.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class SaleListViewController implements Initializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    @FXML
    private TableColumn<Sale, String> identificationColumn;
    @FXML
    private TableColumn<Sale, String> affiliateNameColumn;
    @FXML
    private TableColumn<Sale, String> typeColumn;
    @FXML
    private TableColumn<Sale, String> referenceColumn;
    @FXML
    private TableColumn<Sale, String> quantityColumn;
    @FXML
    private TableColumn<Sale, String> totalColumn;
    @FXML
    private TableColumn<Sale, String> statusColumn;
    @FXML
    private TableColumn<Sale, String> saleDateColumn;
    @FXML
    private TableView<Sale> saleTable;
    @FXML
    private TextField fieldSearch;
    @FXML
    private FontIcon iconSearch;
    @FXML
    private Pagination pagination;
    @FXML
    private Button registerButton;
    @FXML
    private MenuItem cancelMenuItem;

    private final ObservableList<Sale> saleList = FXCollections.observableArrayList();
    private final SaleService saleService = new SaleService();
    private final AffiliateService affiliateService = new AffiliateService();
    private final ProductService productService = new ProductService();

    // Cache simple para no repetir llamadas find-by-id del mismo afiliado/producto
    // dentro de la misma carga de página (el backend de Sale no denormaliza nombres).
    private final Map<Long, Affiliate> affiliateCache = new HashMap<>();
    private final Map<Long, Product> productCache = new HashMap<>();

    private Long currentAffiliateId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable) -> loadCurrentPage());
        configureColumns();
        configureRowFactory();
        applyRolePermissions();
        listAllSales(0);
    }

    private void configureRowFactory() {
        saleTable.setRowFactory(tv -> {
            TableRow<Sale> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    saleTable.getSelectionModel().select(row.getIndex());
                }
            });
            return row;
        });
    }

    private void configureColumns() {
        identificationColumn.setCellValueFactory(data -> new SimpleStringProperty(
                resolveAffiliate(data.getValue().getAffiliateId()).map(Affiliate::getIdentification).orElse("-")));
        affiliateNameColumn.setCellValueFactory(data -> new SimpleStringProperty(
                resolveAffiliate(data.getValue().getAffiliateId())
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .orElse("-")));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        referenceColumn.setCellValueFactory(data -> new SimpleStringProperty(resolveReference(data.getValue())));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        totalColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getTotal() != null ? "$ " + data.getValue().getTotal() : "-"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        saleDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getSaleDate() != null
                        ? data.getValue().getSaleDate().format(DATE_TIME_FORMATTER)
                        : "-"));
    }

    private String resolveReference(Sale sale) {
        if (sale.getType() == SaleType.PRODUCTO) {
            return resolveProduct(sale.getProductId()).map(Product::getName).orElse("-");
        }
        return sale.getAffiliateMembershipId() != null ? "Afiliación #" + sale.getAffiliateMembershipId() : "-";
    }

    private Optional<Affiliate> resolveAffiliate(Long affiliateId) {
        if (affiliateId == null) {
            return Optional.empty();
        }
        Affiliate cached = affiliateCache.computeIfAbsent(affiliateId, id -> {
            ServiceResponse<Affiliate> response = affiliateService.findById(id);
            return response.getCode() == 200 ? response.getData() : null;
        });
        return Optional.ofNullable(cached);
    }

    private Optional<Product> resolveProduct(Long productId) {
        if (productId == null) {
            return Optional.empty();
        }
        Product cached = productCache.computeIfAbsent(productId, id -> {
            ServiceResponse<Product> response = productService.findById(id);
            return response.getCode() == 200 ? response.getData() : null;
        });
        return Optional.ofNullable(cached);
    }

    private void applyRolePermissions() {
        String role = Session.getCurrentUser() != null ? Session.getCurrentUser().getRole() : null;
        boolean canRegister = "ADMINISTRADOR".equals(role) || "RECEPCIONISTA".equals(role);
        boolean canCancel = "ADMINISTRADOR".equals(role);

        registerButton.setVisible(canRegister);
        registerButton.setManaged(canRegister);
        cancelMenuItem.setVisible(canCancel);
        cancelMenuItem.setDisable(!canCancel);
    }

    private void loadCurrentPage() {
        if (currentAffiliateId != null) {
            listByAffiliate(currentAffiliateId, pagination.getCurrentPageIndex());
        } else {
            listAllSales(pagination.getCurrentPageIndex());
        }
    }

    private void listAllSales(int page) {
        ServiceResponse<PageResponse<Sale>> serviceResponse =
                saleService.search(null, null, null, null, null, page);
        applyResult(serviceResponse);
    }

    private void listByAffiliate(Long affiliateId, int page) {
        ServiceResponse<PageResponse<Sale>> serviceResponse =
                saleService.search(affiliateId, null, null, null, null, page);
        applyResult(serviceResponse);
    }

    private void applyResult(ServiceResponse<PageResponse<Sale>> serviceResponse) {
        saleList.clear();

        if (serviceResponse.getCode() == 200) {
            PageResponse<Sale> pageResponse = serviceResponse.getData();
            saleList.addAll(pageResponse.getContent());
            saleTable.setItems(saleList);
            pagination.setPageCount(Math.max(pageResponse.getTotalPages(), 1));
        } else {
            saleTable.setItems(saleList);
            pagination.setPageCount(1);
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo cargar el listado de ventas.");
        }
    }

    @FXML
    private void searchSale(ActionEvent event) {
        String identification = fieldSearch.getText();
        if (identification == null || identification.isBlank()) {
            currentAffiliateId = null;
            listAllSales(0);
            return;
        }

        ServiceResponse<PageResponse<Affiliate>> affiliateResponse = affiliateService.search(identification, null, null, 0);
        if (affiliateResponse.getCode() == 200 && !affiliateResponse.getData().getContent().isEmpty()) {
            currentAffiliateId = affiliateResponse.getData().getContent().get(0).getId();
            listByAffiliate(currentAffiliateId, 0);
        } else {
            AlertUtils.showWarning("No se encontró ningún afiliado con esa identificación.");
            saleList.clear();
            saleTable.setItems(saleList);
            pagination.setPageCount(1);
        }
    }

    @FXML
    private void showSaleCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/sale/SaleCreateView.fxml");
    }

    @FXML
    private void cancelSale(ActionEvent event) {
        Sale selected = saleTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione una venta para anular.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("¿Desea anular la venta #" + selected.getId() + "?");
        if (!confirmed) {
            return;
        }

        ServiceResponse<Sale> response = saleService.cancel(selected.getId());
        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Venta anulada con éxito.");
            loadCurrentPage();
        } else {
            AlertUtils.showError(response.getMessage() != null
                    ? response.getMessage()
                    : "No se pudo anular la venta.");
        }
    }
}
