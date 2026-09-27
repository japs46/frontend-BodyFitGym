package com.japs.frontend.bodyfitgym.controllers.product;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.ProductService;
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
import java.util.ResourceBundle;

public class ProductListViewController implements Initializable {

    @FXML
    private TableColumn<Product, String> nameColumn;
    @FXML
    private TableColumn<Product, String> quantityColumn;
    @FXML
    private TableColumn<Product, String> priceColumn;
    @FXML
    private TableColumn<Product, String> statusColumn;
    @FXML
    private TableView<Product> productTable;
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

    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ProductService productService = new ProductService();

    private String currentName = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable) ->
                listProducts(currentName, pagination.getCurrentPageIndex()));
        configureColumns();
        configureRowFactory();
        applyRolePermissions();
        listProducts(null, 0);
    }

    private void configureColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(
                "$ " + data.getValue().getPrice()));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void configureRowFactory() {
        productTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    productTable.getSelectionModel().select(row.getIndex());
                }
            });
            return row;
        });
    }

    private void applyRolePermissions() {
        String role = Session.getCurrentUser() != null ? Session.getCurrentUser().getRole() : null;
        boolean canCreate = "ADMINISTRADOR".equals(role) || "RECEPCIONISTA".equals(role);
        boolean canManage = "ADMINISTRADOR".equals(role);

        registerButton.setVisible(canCreate);
        registerButton.setManaged(canCreate);
        updateMenuItem.setVisible(canManage);
        updateMenuItem.setDisable(!canManage);
        deleteMenuItem.setVisible(canManage);
        deleteMenuItem.setDisable(!canManage);
    }

    private void listProducts(String name, int page) {
        productList.clear();

        ServiceResponse<PageResponse<Product>> serviceResponse = productService.search(name, null, page);

        if (serviceResponse.getCode() == 200) {
            PageResponse<Product> pageResponse = serviceResponse.getData();
            productList.addAll(pageResponse.getContent());
            productTable.setItems(productList);
            pagination.setPageCount(Math.max(pageResponse.getTotalPages(), 1));
        } else {
            productTable.setItems(productList);
            pagination.setPageCount(1);
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo cargar el listado de productos.");
        }
    }

    @FXML
    private void searchProduct(ActionEvent event) {
        currentName = fieldSearch.getText();
        listProducts(currentName, 0);
    }

    @FXML
    private void showProductCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/product/ProductCreateView.fxml");
    }

    @FXML
    private void showProductUpdateView(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione un producto para actualizar.");
            return;
        }

        MainViewController.getInstance().cargarVistaProductUpdate(selected);
    }

    @FXML
    private void deleteProduct(ActionEvent event) {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtils.showWarning("Seleccione un producto para eliminar.");
            return;
        }

        boolean confirmed = AlertUtils.showConfirm("¿Desea eliminar el producto \"" + selected.getName() + "\"?");
        if (!confirmed) {
            return;
        }

        ServiceResponse<Void> serviceResponse = productService.delete(selected.getId());
        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Producto eliminado con éxito.");
            listProducts(currentName, pagination.getCurrentPageIndex());
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo eliminar el producto.");
        }
    }
}
