package vdtry06.springboot.gateway.api_gateway.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vdtry06.springboot.gateway.api_gateway.dto.ApiResponse;
import vdtry06.springboot.gateway.api_gateway.dto.request.IntrospectRequest;
import vdtry06.springboot.gateway.api_gateway.dto.response.IntrospectResponse;
import vdtry06.springboot.gateway.api_gateway.repository.AuthClientRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    AuthClientRepository authClientRepository;


    public Mono<ApiResponse<IntrospectResponse>> introspect(String token) {
        return authClientRepository.introspect(IntrospectRequest.builder()
                .token(token)
                .build());
    }
}
