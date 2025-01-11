package vdtry06.springboot.gateway.api_gateway.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;
import vdtry06.springboot.gateway.api_gateway.dto.ApiResponse;
import vdtry06.springboot.gateway.api_gateway.dto.request.IntrospectRequest;
import vdtry06.springboot.gateway.api_gateway.dto.response.IntrospectResponse;

public interface AuthClientRepository {

    @PostExchange(url ="/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request);

}
