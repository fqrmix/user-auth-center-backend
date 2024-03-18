package com.fqrmix.authcenterback.services;

import com.fqrmix.authcenterback.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface MyUserDetailsService extends UserDetailsService {
    User save(User user);

    User create(User user);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Boolean existByUsername(String username);
}
