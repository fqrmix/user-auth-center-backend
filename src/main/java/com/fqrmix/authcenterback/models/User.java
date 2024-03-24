package com.fqrmix.authcenterback.models;

import com.fqrmix.authcenterback.dto.response.data.UserDataResponse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;

/**
 * Entity class representing a user.
 */

@Entity
@Builder(setterPrefix = "with")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username")
        })
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    /**
     * Converts the user entity to UserDataResponse DTO.
     *
     * @return UserDataResponse DTO representing the user entity.
     */
    public UserDataResponse toUserDataResponse() {
        return UserDataResponse.builder()
                .withUsername(this.getUsername())
                .withRoles(this.getRoles())
                .build();
    }
}
