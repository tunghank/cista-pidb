package com.cista.pidb.admin.to;

import java.util.ArrayList;
import java.util.List;

/**
 * Database table USER.
 * @author JEE
 *
 */
public class UserTo {

    /**
     * table field.
     */
    private int id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean active;
    private List<RoleTo> roles;

    /**
     * @return the active
     */
    public boolean getActive() {
        return active;
    }

    /**
     * @param _active the active to set
     */
    public void setActive(final boolean _active) {
        this.active = _active;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param _email the email to set
     */
    public void setEmail(final String _email) {
        this.email = _email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param _firstName the firstName to set
     */
    public void setFirstName(final String _firstName) {
        this.firstName = _firstName;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param _id the id to set
     */
    public void setId(final int _id) {
        this.id = _id;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param _lastName the lastName to set
     */
    public void setLastName(final String _lastName) {
        this.lastName = _lastName;
    }

    /**
     * @return the roles
     */
    public List<RoleTo> getRoles() {
        return roles;
    }

    /**
     * @param _roles the roles to set
     */
    public void setRoles(final List<RoleTo> _roles) {
        this.roles = _roles;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param _userId the userId to set
     */
    public void setUserId(final String _userId) {
        this.userId = _userId;
    }
    
    public void addRole(final RoleTo roleTo) {
        if (roles == null) {
            roles = new ArrayList<RoleTo>();
        }

        roles.add(roleTo);
    }
}
