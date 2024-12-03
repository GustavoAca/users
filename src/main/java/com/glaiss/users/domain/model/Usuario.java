package com.glaiss.users.domain.model;

import com.glaiss.core.domain.model.EntityAbstract;
import com.glaiss.core.security.Privilegio;
import com.glaiss.users.controller.dto.LoginRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "usuarios")
@Entity
public class Usuario extends EntityAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Privilegio privilegio;

    public boolean isLoginCorretc(LoginRequest loginRequest) {
        return true;
    }}
