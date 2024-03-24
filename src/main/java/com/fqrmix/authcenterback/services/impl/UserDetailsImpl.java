package com.fqrmix.authcenterback.services.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fqrmix.authcenterback.models.Role;
import com.fqrmix.authcenterback.models.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of UserDetails interface representing user details for authentication.
 */
@Getter
public class UserDetailsImpl implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String username;
    @JsonIgnore
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Set<Role> roles;

    /**
     * Constructs a UserDetailsImpl object with the provided user details.
     *
     * @param id           The user's ID.
     * @param username     The user's username.
     * @param password     The user's password.
     * @param authorities  The authorities granted to the user.
     */
    public UserDetailsImpl(Long id, String username, String password, Set<Role> roles,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.authorities = authorities;

    }

    /**
     * Constructs a UserDetailsImpl object from the given User entity.
     *
     * @param user The User entity from which to construct UserDetailsImpl.
     * @return UserDetailsImpl object representing the provided User entity.
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRoles(),
                authorities);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

}
