package com.japs.frontend.bodyfitgym.controllers.product;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.models.ProductStatus;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.ProductService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductUpdateViewController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;
    @FXML
    private ComboBox<String> statusCombo;

    private final ProductService productService = new ProductService();
    private Product product;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void setProduct(Product product) {
        this.product = product;
        populateForm();
    }

    private void populateForm() {
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        quantityField.setText(product.getQuantity() != null ? product.getQuantity().toString() : "");
        priceField.setText(product.getPrice() != null ? product.getPrice().toString() : "");
        statusCombo.setValue(product.getStatus() != null ? product.getStatus().name() : null);
    }

    @FXML
    private void updateProduct(ActionEvent event) {
        if (nameField.getText() == null || nameField.getText().isBlank()) {
            AlertUtils.showWarning("El nombre del producto es obligatorio.");
            return;
        }

        Integer quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (Exception e) {
            AlertUtils.showWarning("La cantidad en stock debe ser un número entero válido.");
            return;
        }

        BigDecimal price;
        try {
            price = new BigDecimal(priceField.getText().trim());
        } catch (Exception e) {
            AlertUtils.showWarning("El precio debe ser un número válido.");
            return;
        }

        Product updated = new Product();
        updated.setName(nameField.getText().trim());
        updated.setDescription(descriptionField.getText() == null ? null : descriptionField.getText().trim());
        updated.setQuantity(quantity);
        updated.setPrice(price);
        updated.setStatus(statusCombo.getValue() != null ? ProductStatus.valueOf(statusCombo.getValue()) : null);

        ServiceResponse<Product> serviceResponse = productService.update(product.getId(), updated);

        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Producto actualizado exitosamente.");
            showProductListView(event);
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo actualizar el producto.");
        }
    }

    @FXML
    private void showProductListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/product/ProductListView.fxml");
    }
}
