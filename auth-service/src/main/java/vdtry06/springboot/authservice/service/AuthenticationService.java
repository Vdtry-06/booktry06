package vdtry06.springboot.authservice.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import vdtry06.springboot.authservice.dto.request.AuthenticationRequest;
import vdtry06.springboot.authservice.dto.request.IntrospectRequest;
import vdtry06.springboot.authservice.dto.request.LogoutRequest;
import vdtry06.springboot.authservice.dto.request.RefreshRequest;
import vdtry06.springboot.authservice.dto.response.AuthenticationResponse;
import vdtry06.springboot.authservice.dto.response.IntrospectResponse;
import vdtry06.springboot.authservice.entity.InvalidatedToken;
import vdtry06.springboot.authservice.entity.User;
import vdtry06.springboot.authservice.exception.AppException;
import vdtry06.springboot.authservice.exception.ErrorCode;
import vdtry06.springboot.authservice.repository.InvalidatedTokenRepository;
import vdtry06.springboot.authservice.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    // xác thực chữ ký của token. Kiểm tra thời hạn của token. trả về đối tượng chỉ ra token có hợp lệ hay không.
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        // kiểm tra đã đăng nhập hay đã logout.
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    // đăng xuất token
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(signToken.getJWTClaimsSet().getJWTID())
                    .expiryTime(signToken.getJWTClaimsSet().getExpirationTime())
                    .build();

            invalidatedTokenRepository.save(invalidatedToken);

        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    // tạo token mới
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(signedJWT.getJWTClaimsSet().getJWTID())
                .expiryTime(signedJWT.getJWTClaimsSet().getExpirationTime())
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();
        var user = userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHORIZED));

        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier); // true | false: neu token khong bi thay doi true
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHORIZED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        return signedJWT;
    }

    // xác thực người dùng
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("SignKey: {}", SIGNER_KEY);

        // tìm người dùng trong csdl
        var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        // so sánh mật khẩu: mk có độ mạnh càng lớn càng tốn bộ nhớ
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);

        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    // tạo token có thời hạn
    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("vdtry06.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    // xây dựng scope
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(roles -> {
                stringJoiner.add("ROLE_" + roles.getName());
                if (!CollectionUtils.isEmpty(roles.getPermissions()))
                    roles.getPermissions().forEach(permissions -> stringJoiner.add(permissions.getName()));
            });
        }
        return stringJoiner.toString();
    }
}
