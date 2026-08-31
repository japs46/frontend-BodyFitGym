package com.japs.frontend.bodyfitgym.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.japs.frontend.bodyfitgym.models.AuthResponse;
import com.japs.frontend.bodyfitgym.models.LoginRequest;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.utils.ObjectMapperProvider;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    private static final String API_URL = "http://localhost:8080/api/auth";

    public ServiceResponse<AuthResponse> login(String userName, String password) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            String body = ObjectMapperProvider.toJson(new LoginRequest(userName, password));

            HttpResponse<JsonNode> response = Unirest.post(API_URL + "/login")
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .body(body)
                    .asJson();

            ServiceResponse<AuthResponse> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<AuthResponse>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;

        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<AuthResponse> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }
}
