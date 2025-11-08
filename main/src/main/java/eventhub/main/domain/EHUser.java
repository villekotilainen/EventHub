package eventhub.main.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "eh_user")
public class EHUser {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long id;

    @NotEmpty(message = "Username is required")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "Password is required")
    @Column(name = "password_hash", nullable = false)
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String passwordHash;

    @NotEmpty(message = "First name is required")
    @Column(name = "firstname", nullable = false)
    private String firstname;

    @NotEmpty(message = "Last name is required")
    @Column(name = "lastname", nullable = false)
    private String lastname;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private UserRole role;
    
    public EHUser() {
    }
    
    public EHUser(String username, String passwordHash, String firstname, String lastname, String email, UserRole role) {
        super();
        this.username = username;
        this.passwordHash = passwordHash;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.role = role;
    }
    
    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
    
    // ToString

    @Override
    public String toString() {
        return "EHUser [id=" + id + ", username=" + username + ", firstname=" + firstname + ", lastname=" + lastname
                + ", email=" + email + ", role=" + role + "]";
    }
    
}
