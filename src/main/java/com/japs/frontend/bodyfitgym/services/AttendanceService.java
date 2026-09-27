package com.japs.frontend.bodyfitgym.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.japs.frontend.bodyfitgym.models.Attendance;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.utils.ObjectMapperProvider;
import com.japs.frontend.bodyfitgym.utils.Session;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.UUID;

public class AttendanceService {
    private final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
    private static final String API_URL = "http://localhost:8080/api/attendance";

    public ServiceResponse<PageResponse<Attendance>> search(Long affiliateId, LocalDate dateFrom, LocalDate dateTo, int page) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.get(API_URL + "/search")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .queryString("affiliateId", affiliateId)
                    .queryString("dateFrom", dateFrom)
                    .queryString("dateTo", dateTo)
                    .queryString("page", page)
                    .asJson();

            ServiceResponse<PageResponse<Attendance>> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<PageResponse<Attendance>>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<PageResponse<Attendance>> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Attendance> save(Long affiliateId) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            Attendance attendance = new Attendance();
            attendance.setAffiliateId(affiliateId);
            String body = ObjectMapperProvider.toJson(attendance);

            HttpResponse<JsonNode> response = Unirest.post(API_URL + "/save")
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .body(body)
                    .asJson();

            ServiceResponse<Attendance> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Attendance>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Attendance> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }
}
