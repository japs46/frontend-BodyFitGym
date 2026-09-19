package com.japs.frontend.bodyfitgym.controllers.affiliate;

import com.japs.frontend.bodyfitgym.models.Affiliate;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Utilidades compartidas por los controllers de crear/actualizar afiliado:
 * parseo tolerante de fecha/números opcionales y volcado del formulario al modelo.
 */
final class AffiliateFormMapper {

    private AffiliateFormMapper() {
    }

    /** Devuelve la fecha parseada, o null si el texto es vacío o inválido. */
    static LocalDate parseDate(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(text.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String trimOrNull(TextField field) {
        String value = field.getText();
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    /**
     * Vuelca los campos comunes al afiliado. Devuelve un mensaje de error si
     * alguna medida física no es numérica, o null si todo salió bien.
     */
    static String fill(Affiliate affiliate,
                       TextField identification, TextField firstName, TextField middleName,
                       TextField lastName, TextField secondLastName, ComboBox<String> sexCombo,
                       LocalDate birthDate, TextField occupation, TextField mobilePhone,
                       TextField homePhone, TextField email, TextField address,
                       TextField neighborhood, TextField city, TextField postalCode,
                       TextField weight, TextField height, TextField waist,
                       TextField leg, TextField hip) {

        affiliate.setIdentification(trimOrNull(identification));
        affiliate.setFirstName(trimOrNull(firstName));
        affiliate.setMiddleName(trimOrNull(middleName));
        affiliate.setLastName(trimOrNull(lastName));
        affiliate.setSecondLastName(trimOrNull(secondLastName));
        affiliate.setSex(sexCombo.getValue());
        affiliate.setBirthDate(birthDate);
        affiliate.setOccupation(trimOrNull(occupation));
        affiliate.setMobilePhone(trimOrNull(mobilePhone));
        affiliate.setHomePhone(trimOrNull(homePhone));
        affiliate.setEmail(trimOrNull(email));
        affiliate.setAddress(trimOrNull(address));
        affiliate.setNeighborhood(trimOrNull(neighborhood));
        affiliate.setCity(trimOrNull(city));
        affiliate.setPostalCode(trimOrNull(postalCode));

        try {
            affiliate.setWeight(parseDouble(weight));
            affiliate.setHeight(parseDouble(height));
            affiliate.setWaist(parseDouble(waist));
            affiliate.setLeg(parseDouble(leg));
            affiliate.setHip(parseDouble(hip));
        } catch (NumberFormatException e) {
            return "Los datos físicos (peso, estatura, cintura, pierna, cadera) deben ser numéricos.";
        }

        return null;
    }

    private static Double parseDouble(TextField field) {
        String value = field.getText();
        if (value == null || value.isBlank()) {
            return null;
        }
        return Double.parseDouble(value.trim());
    }
}
