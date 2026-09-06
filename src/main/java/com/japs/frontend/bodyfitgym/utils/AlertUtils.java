package com.japs.frontend.bodyfitgym.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Optional;

public class AlertUtils {

    private static Alert crearBase(Alert.AlertType tipo, String titulo, String mensaje, String iconLiteral, String iconColor) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);

        FontIcon icon = new FontIcon(iconLiteral);
        icon.setIconSize(22);
        icon.setIconColor(javafx.scene.paint.Color.web(iconColor));
        alert.getDialogPane().setGraphic(icon);

        alert.getDialogPane().getStylesheets().add(
                AlertUtils.class.getResource("/css/Alert/AlertStyle.css").toExternalForm()
        );
        alert.getDialogPane().getStyleClass().add(getStyleClass(tipo));

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().clear();

        return alert;
    }

    private static String getStyleClass(Alert.AlertType tipo) {
        return switch (tipo) {
            case ERROR -> "alert-error";
            case WARNING -> "alert-warning";
            case CONFIRMATION -> "alert-confirm";
            case INFORMATION -> "alert-info";
            default -> "alert-default";
        };
    }

    public static void showError(String mensaje) {
        crearBase(Alert.AlertType.ERROR, "Error", mensaje, "bi-x-circle-fill", "#E74C3C").showAndWait();
    }

    public static void showSuccess(String mensaje) {
        crearBase(Alert.AlertType.INFORMATION, "Éxito", mensaje, "bi-check-circle-fill", "#2ECC71").showAndWait();
    }

    public static void showWarning(String mensaje) {
        crearBase(Alert.AlertType.WARNING, "Atención", mensaje, "bi-exclamation-triangle-fill", "#F59E0B").showAndWait();
    }

    public static boolean showConfirm(String mensaje) {
        Alert alert = crearBase(Alert.AlertType.CONFIRMATION, "Confirmar", mensaje, "bi-question-circle-fill", "#2ECC71");
        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
}