package com.japs.frontend.bodyfitgym.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.japs.frontend.bodyfitgym.models.User;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.utils.ObjectMapperProvider;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private static final String API_URL = "http://localhost:8080/api/user";

    public ServiceResponse<List<User>> findAll() {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.get(API_URL + "/find-all")
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .asJson();

            ServiceResponse<List<User>> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<List<User>>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;

        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<List<User>> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());
            errorResponse.setData(Collections.emptyList());

            return errorResponse;
        }
    }
    public ServiceResponse<PageResponse<User>> searchUser(String name, String userName, String document,int page){

        String correlationId = UUID.randomUUID().toString();

        logger.info("correlationId: {} ", correlationId);

        HttpResponse<JsonNode> response = Unirest.get(API_URL+"/search")
                .header("X-Correlation-Id", correlationId)
                .queryString("name", name)
                .queryString("userName", userName)
                .queryString("document", document)
                .queryString("page", page)
                .asJson();
        try{
            ServiceResponse<PageResponse<User>> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<PageResponse<User>>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<PageResponse<User>> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

}
