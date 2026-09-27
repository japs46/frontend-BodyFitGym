package com.japs.frontend.bodyfitgym.controllers.product;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.ProductService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.math.BigDecimal;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductCreateViewController implements Initializable {

    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField priceField;

    private final ProductService productService = new ProductService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    private void saveProduct(ActionEvent event) {
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

        Product product = new Product();
        product.setName(nameField.getText().trim());
        product.setDescription(descriptionField.getText() == null ? null : descriptionField.getText().trim());
        product.setQuantity(quantity);
        product.setPrice(price);

        ServiceResponse<Product> serviceResponse = productService.save(product);

        if (serviceResponse.getCode() == 200) {
            AlertUtils.showSuccess("Producto registrado exitosamente.");
            showProductListView(event);
        } else {
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo registrar el producto.");
        }
    }

    @FXML
    private void cleanForm(ActionEvent event) {
        nameField.clear();
        descriptionField.clear();
        quantityField.clear();
        priceField.clear();
    }

    @FXML
    private void showProductListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/product/ProductListView.fxml");
    }
}
