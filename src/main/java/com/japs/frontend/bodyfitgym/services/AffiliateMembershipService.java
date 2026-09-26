package com.japs.frontend.bodyfitgym.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.japs.frontend.bodyfitgym.models.AffiliateMembership;
import com.japs.frontend.bodyfitgym.models.SubscriptionStatus;
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

public class AffiliateMembershipService {
    private final Logger logger = LoggerFactory.getLogger(AffiliateMembershipService.class);
    private static final String API_URL = "http://localhost:8080/api/affiliate-membership";

    public ServiceResponse<PageResponse<AffiliateMembership>> search(Long affiliateId, Long membershipId,
                                                                       SubscriptionStatus status, int page) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.get(API_URL + "/search")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .queryString("affiliateId", affiliateId)
                    .queryString("membershipId", membershipId)
                    .queryString("status", status)
                    .queryString("page", page)
                    .asJson();

            ServiceResponse<PageResponse<AffiliateMembership>> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<PageResponse<AffiliateMembership>>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<PageResponse<AffiliateMembership>> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<AffiliateMembership> save(AffiliateMembership affiliateMembership) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            String body = ObjectMapperProvider.toJson(affiliateMembership);

            HttpResponse<JsonNode> response = Unirest.post(API_URL + "/save")
                    .header("Content-Type", "application/json")
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .body(body)
                    .asJson();

            ServiceResponse<AffiliateMembership> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<AffiliateMembership>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (JsonProcessingException e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<AffiliateMembership> errorResponse = new ServiceResponse<>();
            errorResponse.setCode(500);
            errorResponse.setMessage("Error al conectar con el servicio: " + e.getMessage());

            return errorResponse;
        }
    }

    public ServiceResponse<AffiliateMembership> freeze(Long id) {
        return postAction(id, "/freeze/");
    }

    public ServiceResponse<AffiliateMembership> unfreeze(Long id) {
        return postAction(id, "/unfreeze/");
    }

    private ServiceResponse<AffiliateMembership> postAction(Long id, String actionPath) {
        String correlationId = UUID.randomUUID().toString();
        logger.info("correlationId: {} ", correlationId);

        try {
            HttpResponse<JsonNode> response = Unirest.post(API_URL + actionPath + id)
                    .header("X-Correlation-Id", correlationId)
                    .header("Authorization", Session.getAuthorizationHeader())
                    .asJson();

            ServiceResponse<AffiliateMembership> serviceResponse = ObjectMapperProvider.readValue(
                    response.getBody().toString(),
                    new TypeReference<ServiceResponse<AffiliateMembership>>() {}
            );
            serviceResponse.setCode(response.getStatus());

            logger.info("correlationId: {}, response api: {}", correlationId, serviceResponse.toString());

            return serviceResponse;
        } catch (Exception e) {
            logger.error("correlationId: {}, error: {}", correlationId, e.getMessage());

            ServiceResponse<AffiliateMembership> errorResponse = new ServiceResponse<>();
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
