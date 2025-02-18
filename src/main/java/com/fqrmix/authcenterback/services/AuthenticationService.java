package com.fqrmix.authcenterback.services;

import com.fqrmix.authcenterback.dto.request.LoginRequestDTO;
import com.fqrmix.authcenterback.dto.request.RegisterRequestDTO;
import com.fqrmix.authcenterback.dto.response.data.TokenResponse;
import com.fqrmix.authcenterback.dto.response.data.UserDataResponse;
import com.fqrmix.authcenterback.models.enums.ERole;
import com.fqrmix.authcenterback.models.User;
import com.fqrmix.authcenterback.models.Role;
import com.fqrmix.authcenterback.repositories.RoleRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;


/**
 * Service class responsible for handling user authentication and registration.
 */

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    @Autowired
    private final MyUserDetailsService userDetailsService;

    @Autowired
    private final JWTService jwtService;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final AuthenticationManager authenticationManager;

    /**
     * Authenticates a user based on the provided credentials
     * and generates a JWT token upon successful authentication.
     *
     * @param requestDTO The DTO containing login credentials.
     * @return A response indicating the success of the login operation along with a JWT token.
     */
    public TokenResponse login(LoginRequestDTO requestDTO) throws AuthenticationException {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDTO.getUsername(),
                        requestDTO.getPassword()
                )
        );

        log.info(
                "User was successfully authenticated with following credentials: {}",
                authentication.getPrincipal()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var jwtToken = jwtService.generateJwtToken(authentication);
        log.info(
                "JWT token for user: {} was successfully generated. Data: {}",
                authentication.getPrincipal(),
                jwtToken
        );

        return TokenResponse.builder()
                .withToken(jwtToken)
                .withExpirationDate(jwtService.getExpirationDateFromJwtToken(jwtToken))
                .withType("JWT")
                .build();
    }

    /**
     * Registers a new user with the application.
     *
     * @param requestDTO The DTO containing registration details.
     * @return A response indicating the success of the registration operation.
     * @throws RuntimeException If a user with the same username already exists.
     */
    public UserDataResponse register(RegisterRequestDTO requestDTO) throws RuntimeException {

        var user = User.builder()
                .withUsername(requestDTO.getUsername())
                .withPassword(passwordEncoder.encode(requestDTO.getPassword()))
                .build();

        Set<ERole> eRoles = requestDTO.getRoles();
        Set<Role> roles = new HashSet<>();

        if (eRoles == null) {
            throw new ValidationException("Roles set is empty in request DTO");
        }

        eRoles.forEach(role -> {
            Role adminRole = roleRepository.findByName(role)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
        });

        user.setRoles(roles);
        userDetailsService.create(user);

        log.info("User was successfully registered with following data: {}", user.toUserDataResponse());

        return user.toUserDataResponse();
    }
}


