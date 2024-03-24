package com.fqrmix.authcenterback.services.impl;

import com.fqrmix.authcenterback.models.User;
import com.fqrmix.authcenterback.repositories.UserRepository;
import com.fqrmix.authcenterback.services.MyUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fqrmix.authcenterback.exception.UserAlreadyExistsException;

/**
 * Implementation of MyUserDetailsService interface providing user-related services.
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements MyUserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Saves the provided user entity.
     *
     * @param user The user entity to save.
     * @return The saved user entity.
     */
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Creates a new user with the provided user entity.
     *
     * @param user The user entity to create.
     * @return The created user entity.
     * @throws RuntimeException If a user with the same username already exists.
     */
    @Override
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            log.warn("Creation failed. User with username { {} } already exists", user.getUsername());
            throw new UserAlreadyExistsException("User with this username already exists");
        }

        return save(user);
    }

    /**
     * Loads user details by username.
     *
     * @param username The username to load user details for.
     * @return UserDetails object representing the user.
     * @throws UsernameNotFoundException If the user with the specified username is not found.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }

    /**
     * Checks if a user exists with the given username.
     *
     * @param username The username to check.
     * @return True if a user with the given username exists; false otherwise.
     */
    @Override
    public Boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}

