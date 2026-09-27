package com.japs.frontend.bodyfitgym.controllers.attendance;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.Attendance;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.services.AttendanceService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AttendanceCreateViewController implements Initializable {

    @FXML
    private TextField identificationField;
    @FXML
    private Button searchButton;
    @FXML
    private Label affiliateFoundLabel;
    @FXML
    private Button registerButton;

    private final AttendanceService attendanceService = new AttendanceService();
    private final AffiliateService affiliateService = new AffiliateService();

    private Affiliate foundAffiliate;

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
        } else {
            foundAffiliate = null;
            affiliateFoundLabel.setText("");
            AlertUtils.showWarning("No se encontró ningún afiliado con esa identificación.");
        }
    }

    @FXML
    private void registerAttendance(ActionEvent event) {
        if (foundAffiliate == null) {
            AlertUtils.showWarning("Busque y confirme un afiliado antes de registrar.");
            return;
        }

        ServiceResponse<Attendance> response = attendanceService.save(foundAffiliate.getId());

        if (response.getCode() == 200) {
            AlertUtils.showSuccess("Asistencia registrada exitosamente.");
            showAttendanceListView(event);
        } else {
            AlertUtils.showError(response.getMessage() != null
                    ? response.getMessage()
                    : "No se pudo registrar la asistencia.");
        }
    }

    @FXML
    private void showAttendanceListView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/attendance/AttendanceListView.fxml");
    }
}
