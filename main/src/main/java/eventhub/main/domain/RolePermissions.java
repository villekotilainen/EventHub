package eventhub.main.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class RolePermissions {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String rolePermissionsDescription;

    private boolean canCreateEvent;
    private boolean canEditEvent;
    private boolean canDeleteEvent;
    private boolean canManageUsers;

    public RolePermissions() {}

    public RolePermissions(Long id, String rolePermissionsDescription, boolean canCreateEvent, boolean canEditEvent, boolean canDeleteEvent,
            boolean canManageUsers) {
        super();
        this.id = id;
        this.rolePermissionsDescription = rolePermissionsDescription;
        this.canCreateEvent = canCreateEvent;
        this.canEditEvent = canEditEvent;
        this.canDeleteEvent = canDeleteEvent;
        this.canManageUsers = canManageUsers;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public String getRolePermissionsDescription() {
        return rolePermissionsDescription;
    }

    public void setRolePermissionsDescription(String rolePermissionsDescription) {
        this.rolePermissionsDescription = rolePermissionsDescription;
    }

    public boolean isCanCreateEvent() {
        return canCreateEvent;
    }

    public void setCanCreateEvent(boolean canCreateEvent) {
        this.canCreateEvent = canCreateEvent;
    }

    public boolean isCanEditEvent() {
        return canEditEvent;
    }

    public void setCanEditEvent(boolean canEditEvent) {
        this.canEditEvent = canEditEvent;
    }

    public boolean isCanDeleteEvent() {
        return canDeleteEvent;
    }

    public void setCanDeleteEvent(boolean canDeleteEvent) {
        this.canDeleteEvent = canDeleteEvent;
    }

    public boolean isCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }
    

    // ToString

    @Override
    public String toString() {
        return "RolePermissions [id=" + id + ", canCreateEvent=" + canCreateEvent + ", canEditEvent=" + canEditEvent
                + ", canDeleteEvent=" + canDeleteEvent + ", canManageUsers=" + canManageUsers + "]";
    }
}
