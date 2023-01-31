package com.fieldright.fr.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Email(message = "This email is not valid")
    @NotBlank(message = "The email must not be blank")
    private String email;
    @Size(min = 2)
    private String firstName;
    @Size(min = 2)
    private String lastName;
    private String perfil;
    @Size(min = 11, max = 11)
    private String phone;
    private String avatar;
    @NotBlank
    private String password;
    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    private boolean active = false;
    private boolean alterPassword = true;
    private String exponentPushToken;
    private Long categoria;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return email.equals(usuario.email) &&
                firstName.equals(usuario.firstName) &&
                Objects.equals(lastName, usuario.lastName) &&
                createdAt.equals(usuario.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, createdAt);
    }

    public String getFullName() {
        StringBuilder builder = new StringBuilder();

        if (firstName != null) {
            builder.append(firstName);
            builder.append(" ");
        }

        if (lastName != null) {
            builder.append(lastName);
        }

        return builder.toString();
    }

    public static class Builder {
        private long id;
        private String email;
        private String firstName;
        private String lastName;
        private String password;
        private Timestamp createdAt;
        private boolean active;
        private boolean alterPassword;

        public Builder withId(long id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder createdAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Builder alterPassword(boolean alterPassword) {
            this.alterPassword = alterPassword;
            return this;
        }

        public Usuario build() {
            Usuario usuario = new Usuario();

            usuario.id = this.id;
            usuario.email = this.email;
            usuario.firstName = this.firstName;
            usuario.lastName = this.lastName;
            usuario.password = this.password;
            usuario.createdAt = this.createdAt;
            usuario.active = this.active;
            usuario.alterPassword = this.alterPassword;

            return usuario;
        }
    }
}
