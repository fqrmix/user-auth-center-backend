package com.fqrmix.authcenterback.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
public class Service {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_id_seq")
    @SequenceGenerator(name = "service_id_seq", sequenceName = "service_id_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "username", unique = true, nullable = false)
    @NotBlank
    private EService name;
}
