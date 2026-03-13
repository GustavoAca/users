package com.glaiss.users.domain.model;

import com.glaiss.core.common.builder.IBuilder;
import com.glaiss.core.domain.model.EntityAbstract;
import com.glaiss.core.security.Privilegio;
import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "usuarios")
@Entity
public class Usuario extends EntityAbstract {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String nome;
    @com.fasterxml.jackson.annotation.JsonIgnore
    private String password;

    @Enumerated(EnumType.STRING)
    private Privilegio privilegio;

    @Column(name = "is_ativo")
    private Boolean isAtivo = Boolean.TRUE;

    // Construtor protegido para JPA
    protected Usuario() {
    }

    // Construtor privado para o Builder
    private Usuario(UsuarioBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.nome = builder.nome;
        this.password = builder.password;
        this.privilegio = builder.privilegio;
        this.isAtivo = builder.isAtivo;
        this.setCreatedDate(builder.createdDate);
        this.setCreatedBy(builder.createdBy);
        this.setModifiedDate(builder.modifiedDate);
        this.setModifiedBy(builder.modifiedBy);
        this.setVersion(builder.version);
    }

    // Getters e Setters manuais
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Privilegio getPrivilegio() {
        return privilegio;
    }

    public void setPrivilegio(Privilegio privilegio) {
        this.privilegio = privilegio;
    }

    public Boolean getIsAtivo() {
        return isAtivo;
    }

    public void setIsAtivo(Boolean isAtivo) {
        this.isAtivo = isAtivo;
    }

    public boolean isLoginCorretc(String password, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return bCryptPasswordEncoder.matches(password, this.password);
    }

    // Método estático para iniciar o builder
    public static UsuarioBuilder builder() {
        return new UsuarioBuilder();
    }

    // Implementação manual do Builder
    public static final class UsuarioBuilder implements IBuilder<Usuario> {
        private UUID id;
        private String username;
        private String nome;
        private String password;
        private Privilegio privilegio;
        private Boolean isAtivo = Boolean.TRUE;
        private LocalDateTime createdDate;
        private String createdBy;
        private LocalDateTime modifiedDate;
        private String modifiedBy;
        private Long version;

        private UsuarioBuilder() {
        }

        public UsuarioBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public UsuarioBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UsuarioBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public UsuarioBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UsuarioBuilder privilegio(Privilegio privilegio) {
            this.privilegio = privilegio;
            return this;
        }

        public UsuarioBuilder isAtivo(Boolean isAtivo) {
            this.isAtivo = isAtivo;
            return this;
        }

        public UsuarioBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public UsuarioBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public UsuarioBuilder modifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this;
        }

        public UsuarioBuilder modifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public UsuarioBuilder version(Long version) {
            this.version = version;
            return this;
        }

        @Override
        public Usuario build() {
            return new Usuario(this);
        }
    }
}
