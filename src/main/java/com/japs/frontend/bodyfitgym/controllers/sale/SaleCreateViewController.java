package com.japs.frontend.bodyfitgym.controllers.sale;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.AffiliateMembership;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.models.ProductStatus;
import com.japs.frontend.bodyfitgym.models.Sale;
import com.japs.frontend.bodyfitgym.models.SaleType;
import com.japs.frontend.bodyfitgym.models.SubscriptionStatus;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateMembershipService;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.services.ProductService;
import com.japs.frontend.bodyfitgym.services.SaleService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class SaleCreateViewController implements Initializable {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private TextField identificationField;
    @FXML
    private Label affiliateFoundLabel;
    @FXML
    private RadioButton productTypeRadio;
    @FXML
    private RadioButton affiliationTypeRadio;
    @FXML
    private Pane productSection;
    @FXML
    private Pane affiliationSection;
    @FXML
    private TextField productNameField;
    @FXML
    private Label productFoundLabel;
    @FXML
    private TextField quantityField;
    @FXML
    private Label affiliationFoundLabel;
    @FXML
    private TextField observationField;

    private final AffiliateService affiliateService = new AffiliateService();
    private final ProductService productService = new ProductService();
    private final AffiliateMembershipService affiliateMembershipService = new AffiliateMembershipService();
    private final SaleService saleService = new SaleService();

    private Affiliate foundAffiliate;
    private Product foundProduct;
    private AffiliateMembership foundAffiliateMembership;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
            if (affiliationTypeRadio.isSelected()) {
                loadActiveAffiliateMembership();
            }
        } else {
            foundAffiliate = null;
            affiliateFoundLabel.setText("");
            AlertUtils.showWarning("No se encontró ningún afiliado con esa identificación.");
        }
    }

    @FXML
    private void updateTypeSection(ActionEvent event) {
        boolean isProduct = productTypeRadio.isSelected();
        productSection.setVisible(isProduct);
        productSection.setManaged(isProduct);
        affiliationSection.setVisible(!isProduct);
        affiliationSection.setManaged(!isProduct);

        if (!isProduct) {
            loadActiveAffiliateMembership();
        }
    }

    @FXML
    private void searchProduct(ActionEvent event) {
        String name = productNameField.getText();
        if (name == null || name.isBlank()) {
            AlertUtils.showWarning("Ingrese el nombre del producto a buscar.");
            return;
        }

        ServiceResponse<PageResponse<Product>> response = productService.search(name, ProductStatus.ACTIVO, 0);
        if (response.getCode() == 200 && !response.getData().getContent().isEmpty()) {
            foundProduct = response.getData().getContent().get(0);
            productFoundLabel.setText("Encontrado: " + foundProduct.getName() + " - $ " + foundProduct.getPrice()
                    + " (stock: " + foundProduct.getQuantity() + ")");
        } else {
            foundProduct = null;
            productFoundLabel.setText("");
            AlertUtils.showWarning("No se encontró ningún producto activo con ese nombre.");
        }
    }

    private void loadActiveAffiliateMembership() {
        foundAffiliateMembership = null;
        affiliationFoundLabel.setText("Busque un afiliado para ver su afiliación activa.");

        if (foundAffiliate == null) {
            return;
        }

        ServiceResponse<PageResponse<AffiliateMembership>> response =
                affiliateMembershipService.search(foundAffiliate.getId(), null, SubscriptionStatus.ACTIVA, 0);

        if (response.getCode() == 200 && !response.getData().getContent().isEmpty()) {
            foundAffiliateMembership = response.getData().getContent().get(0);
            affiliationFoundLabel.setText("Afiliación #" + foundAffiliateMembership.getId()
                    + " - vigente hasta " + foundAffiliateMembership.getEndDate().format(DATE_FORMATTER));
        } else {
            affiliationFoundLabel.setText("El afiliado no tiene una afiliación activa.");
        }
    }

    @FXML
    private void registerSale(ActionEvent event) {
        if (foundAffiliate == null) {
            AlertUtils.showWarning("Busque y confirme un afiliado antes de registrar.");
            return;
        }

        Sale sale = new Sale();
        sale.setAffiliateId(foundAffiliate.getId());
        sale.setObservation(observationField.getText() == null ? null : observationField.getText().trim());

        if (productTypeRadio.isSelected()) {
            if (foundProduct == null) {
                AlertUtils.showWarning("Busque y confirme un producto antes de registrar.");
                return;
            }

            Integer quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText().trim());
            } catch (Exception e) {
                AlertUtils.showWarning("La cantidad debe ser un número entero válido.");
                return;
            }

            sale.setType(SaleType.PRODUCTO);
            sale.setProductId(foundProduct.getId());
            sale.setQuantity(quantity);
        } else {
            if (foundAffiliateMembership == null) {
                AlertUtils.showWarning("El afiliado no tiene una afiliación activa para pagar.");
                return;
            }

            sale.setType(SaleType.AFILIACION);
            sale.setAffiliateMembershipId(foundAffiliateMembership.getId());
        }

        ServiceResponse<Sale> response = saleService.save(sale);

        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Venta registrada exitosamente. Total: $ " + response.getData().getTotal());
            showSaleListView(event);
        } else {
            AlertUtils.showError(response.getMessage() != null
                    ? response.getMessage()
                    : "No se pudo registrar la venta.");
        }
    }

    @FXML
    private void showSaleListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/sale/SaleListView.fxml");
    }
}
