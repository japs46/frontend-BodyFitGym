package com.japs.frontend.bodyfitgym.controllers.attendance;

import com.japs.frontend.bodyfitgym.controllers.main.MainViewController;
import com.japs.frontend.bodyfitgym.models.Affiliate;
import com.japs.frontend.bodyfitgym.models.Attendance;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.services.AffiliateService;
import com.japs.frontend.bodyfitgym.services.AttendanceService;
import com.japs.frontend.bodyfitgym.utils.AlertUtils;
import com.japs.frontend.bodyfitgym.utils.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class AttendanceListViewController implements Initializable {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm a");

    @FXML
    private TableColumn<Attendance, String> identificationColumn;
    @FXML
    private TableColumn<Attendance, String> nameColumn;
    @FXML
    private TableColumn<Attendance, String> attendanceDateColumn;
    @FXML
    private TableView<Attendance> attendanceTable;
    @FXML
    private TextField fieldSearch;
    @FXML
    private FontIcon iconSearch;
    @FXML
    private Pagination pagination;
    @FXML
    private Button registerButton;

    private final ObservableList<Attendance> attendanceList = FXCollections.observableArrayList();
    private final AttendanceService attendanceService = new AttendanceService();
    private final AffiliateService affiliateService = new AffiliateService();

    // Cache simple para no repetir llamadas find-by-id del mismo afiliado dentro
    // de la misma carga de página (el backend de Attendance no denormaliza nombres).
    private final Map<Long, Affiliate> affiliateCache = new HashMap<>();

    private Long currentAffiliateId = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagination.currentPageIndexProperty().addListener((observable) -> loadCurrentPage());
        configureColumns();
        applyRolePermissions();
        listTodayAttendances();
    }

    private void configureColumns() {
        identificationColumn.setCellValueFactory(data -> new SimpleStringProperty(
                resolveAffiliate(data.getValue().getAffiliateId()).map(Affiliate::getIdentification).orElse("-")));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(
                resolveAffiliate(data.getValue().getAffiliateId())
                        .map(a -> a.getFirstName() + " " + a.getLastName())
                        .orElse("-")));
        attendanceDateColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getAttendanceDate() != null
                        ? data.getValue().getAttendanceDate().format(DATE_TIME_FORMATTER)
                        : "-"));
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

    private void applyRolePermissions() {
        String role = Session.getCurrentUser() != null ? Session.getCurrentUser().getRole() : null;
        boolean canRegister = "ADMINISTRADOR".equals(role) || "RECEPCIONISTA".equals(role);

        registerButton.setVisible(canRegister);
        registerButton.setManaged(canRegister);
    }

    private void loadCurrentPage() {
        if (currentAffiliateId != null) {
            listByAffiliate(currentAffiliateId, pagination.getCurrentPageIndex());
        } else {
            listToday(pagination.getCurrentPageIndex());
        }
    }

    private void listTodayAttendances() {
        currentAffiliateId = null;
        listToday(0);
    }

    private void listToday(int page) {
        LocalDate today = LocalDate.now();
        ServiceResponse<PageResponse<Attendance>> serviceResponse =
                attendanceService.search(null, today, today, page);
        applyResult(serviceResponse);
    }

    private void listByAffiliate(Long affiliateId, int page) {
        ServiceResponse<PageResponse<Attendance>> serviceResponse =
                attendanceService.search(affiliateId, null, null, page);
        applyResult(serviceResponse);
    }

    private void applyResult(ServiceResponse<PageResponse<Attendance>> serviceResponse) {
        attendanceList.clear();

        if (serviceResponse.getCode() == 200) {
            PageResponse<Attendance> pageResponse = serviceResponse.getData();
            attendanceList.addAll(pageResponse.getContent());
            attendanceTable.setItems(attendanceList);
            pagination.setPageCount(Math.max(pageResponse.getTotalPages(), 1));
        } else {
            attendanceTable.setItems(attendanceList);
            pagination.setPageCount(1);
            AlertUtils.showError(serviceResponse.getMessage() != null
                    ? serviceResponse.getMessage()
                    : "No se pudo cargar el listado de asistencias.");
        }
    }

    @FXML
    private void searchAttendance(ActionEvent event) {
        String identification = fieldSearch.getText();
        if (identification == null || identification.isBlank()) {
            listTodayAttendances();
            return;
        }

        ServiceResponse<PageResponse<Affiliate>> affiliateResponse = affiliateService.search(identification, null, null, 0);
        if (affiliateResponse.getCode() == 200 && !affiliateResponse.getData().getContent().isEmpty()) {
            currentAffiliateId = affiliateResponse.getData().getContent().get(0).getId();
            listByAffiliate(currentAffiliateId, 0);
        } else {
            AlertUtils.showWarning("No se encontró ningún afiliado con esa identificación.");
            attendanceList.clear();
            attendanceTable.setItems(attendanceList);
            pagination.setPageCount(1);
        }
    }

    @FXML
    private void showAttendanceCreateView(ActionEvent event) {
        MainViewController.getInstance().cargarVista("/templates/attendance/AttendanceCreateView.fxml");
    }
}
