package com.glaiss.users.domain.model;

import com.glaiss.core.domain.model.EntityAbstract;
import com.glaiss.core.security.Privilegio;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    private String nome;
    private String password;

    @Enumerated(EnumType.STRING)
    private Privilegio privilegio;

    @Column(name = "is_ativo")
    @Builder.Default
    private Boolean isAtivo = Boolean.TRUE;

    public boolean isLoginCorretc(String password, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(password, this.password);
    }
}
