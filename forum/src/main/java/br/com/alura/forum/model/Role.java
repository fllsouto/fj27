package br.com.alura.forum.model;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Role implements GrantedAuthority {

    @Id
    private String authority;

    private static final Role ROLE_ADMIN = new Role("ROLE_ADMIN");
    private static final Role ROLE_ALUNO = new Role("ROLE_ALUNO");

    /**
     * @deprecated hibernate only
     */
    public Role() {
    }

    public Role(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(authority, role.authority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authority);
    }
}
