package com.japs.frontend.bodyfitgym.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.japs.frontend.bodyfitgym.models.Product;
import com.japs.frontend.bodyfitgym.models.ProductStatus;
import com.japs.frontend.bodyfitgym.response.PageResponse;
import com.japs.frontend.bodyfitgym.response.ServiceResponse;
import com.japs.frontend.bodyfitgym.utils.ObjectMapperProvider;
import com.japs.frontend.bodyfitgym.utils.Session;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class ProductService {
    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final String API_URL = "http://localhost:8080/api/product";

    public ServiceResponse<PageResponse<Product>> search(String name, ProductStatus status, int page) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.get(API_URL + "/search")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .queryString("name", name)
                    .queryString("status", status)
                    .queryString("page", page)
                    .asJson();

            ServiceResponse<PageResponse<Product>> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<PageResponse<Product>>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<PageResponse<Product>> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Product> findById(Long id) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.get(API_URL + "/find-by-id/" + id)
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .asJson();

            ServiceResponse<Product> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Product>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Product> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Product> save(Product product) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            String body = ObjectMapperProvider.toJson(product);

            HttpResponse<JsonNode> response = Unirest.post(API_URL + "/save")
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .body(body)
                    .asJson();

            ServiceResponse<Product> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Product>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Product> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Product> update(Long id, Product product) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            String body = ObjectMapperProvider.toJson(product);

            HttpResponse<JsonNode> response = Unirest.put(API_URL + "/update/" + id)
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .body(body)
                    .asJson();

            ServiceResponse<Product> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Product>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Product> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<Void> delete(Long id) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.delete(API_URL + "/delete/" + id)
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .asJson();

            ServiceResponse<Void> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<Void>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<Void> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }
}
