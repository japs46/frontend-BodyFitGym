package com.japs.frontend.bodyfitgym.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.japs.frontend.bodyfitgym.models.Sale;
import com.japs.frontend.bodyfitgym.models.SaleStatus;
import com.japs.frontend.bodyfitgym.models.SaleType;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.utils.ObjectMapperProvider;
import com.japs.frontend.bodyfitgym.utils.Session;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

public class SaleService {
    private final Logger logger = LoggerFactory.getLogger(SaleService.class);
    private static final String API_URL = "http://localhost:8080/api/sale";

    public ServiceResponse<PageResponse<Sale>> search(Long affiliateId, SaleType type, SaleStatus status,
                                                        LocalDateTime dateFrom, LocalDateTime dateTo, int page) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.get(API_URL + "/search")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .queryString("affiliateId", affiliateId)
                    .queryString("type", type)
                    .queryString("status", status)
                    .queryString("dateFrom", dateFrom)
                    .queryString("dateTo", dateTo)
                    .queryString("page", page)
                    .asJson();

            ServiceResponse<PageResponse<Sale>> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<PageResponse<Sale>>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<PageResponse<Sale>> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Sale> save(Sale sale) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            String body = ObjectMapperProvider.toJson(sale);

            HttpResponse<JsonNode> response = Unirest.post(API_URL + "/save")
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .body(body)
                    .asJson();

            ServiceResponse<Sale> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Sale>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Sale> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Sale> cancel(Long id) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.put(API_URL + "/cancel/" + id)
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .asJson();

            ServiceResponse<Sale> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Sale>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Sale> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }
}
