package edu.icet.ecom.util;

public enum Role {
    ROLE_ADMIN,
    ROLE_USER,
    ROLE_CASHIER,
    ROLE_WAITER,
    ROLE_CHEF;

    public String authority(){
        return this.name();
    }
}