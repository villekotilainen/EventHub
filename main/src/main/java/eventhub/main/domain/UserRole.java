package eventhub.main.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class UserRole {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    @ManyToOne
    private RolePermissions rolePermissions;

    public UserRole() {}

    public UserRole(String roleName, RolePermissions rolePermissions) {
        super();
        this.roleName = roleName;
        this.rolePermissions = rolePermissions;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public RolePermissions getRolePermissions() {
        return rolePermissions;
    }

    public void setRolePermissions(RolePermissions rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    // ToString

    @Override
    public String toString() {
        return "UserRole [id=" + id + ", roleName=" + roleName + ", rolePermissions=" + rolePermissions + "]";
    }
}
