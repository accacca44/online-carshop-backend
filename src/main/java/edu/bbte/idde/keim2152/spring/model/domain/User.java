package edu.bbte.idde.keim2152.spring.model.domain;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {
    private String fullName;
    private String email;
    private String username;
    private String password;
    private String role;
    private String lang;
    private String theme;
    
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<CarListing> carListings;
    
    public User(Long id) {
        super();
        this.id = id;
    }

    public User(String username, String email, String password, String role) {
        super();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
