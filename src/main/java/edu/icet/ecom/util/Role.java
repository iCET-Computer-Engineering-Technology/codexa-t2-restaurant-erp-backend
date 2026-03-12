package edu.icet.ecom.util;

public enum Role {
    ROLE_ADMIN,
    ROLE_USER;

    public String authority(){
        return this.name();
    }
}
