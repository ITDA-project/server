package com.itda.moamoa.domain.chat.interceptor;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private final String username;

    public StompPrincipal(String username){
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
