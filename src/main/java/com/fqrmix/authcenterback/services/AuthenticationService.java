package com.fqrmix.authcenterback.services;

import com.fqrmix.authcenterback.dto.request.LoginRequestDTO;
import com.fqrmix.authcenterback.dto.request.RegisterRequestDTO;
import com.fqrmix.authcenterback.dto.response.BaseResponse;
import com.fqrmix.authcenterback.dto.response.api.impl.ApiSuccessResponse;
import com.fqrmix.authcenterback.dto.response.data.impl.JwtAuthenticationResponse;
import com.fqrmix.authcenterback.models.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * Service class responsible for handling user authentication and registration.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Autowired
    private final MyUserDetailsService userDetailsService;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user based on the provided credentials and generates a JWT token upon successful authentication.
     *
     * @param requestDTO The DTO containing login credentials.
     * @return A response indicating the success of the login operation along with a JWT token.
     */
    public BaseResponse login(LoginRequestDTO requestDTO) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getUsername(),
                        requestDTO.getPassword()
                )
        );

        log.info("User was successfully authenticated with following credentials: {}", authentication.getPrincipal());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtToken = jwtService.generateJwtToken(authentication);
        log.info("JWT token for user: {} was successfully generated. Data: {}", authentication.getPrincipal(), jwtToken);

        JwtAuthenticationResponse jwtResponse = JwtAuthenticationResponse.builder()
                .withToken(jwtToken)
                .withType("Bearer")
                .build();

        return ApiSuccessResponse.builder()
                .withType("success")
                .withData(jwtResponse)
                .build();
    }

    /**
     * Registers a new user with the application.
     *
     * @param requestDTO The DTO containing registration details.
     * @return A response indicating the success of the registration operation.
     * @throws RuntimeException If a user with the same username already exists.
     */
    public BaseResponse register(RegisterRequestDTO requestDTO) throws RuntimeException {

        var user = User.builder()
                .withUsername(requestDTO.getUsername())
                .withPassword(passwordEncoder.encode(requestDTO.getPassword()))
                .build();

        userDetailsService.create(user);

        log.info("User was successfully registered with following data: {}", user.toUserDataResponse());

        return ApiSuccessResponse.builder()
                .withType("success")
                .withData(user.toUserDataResponse())
                .build();
    }
}


