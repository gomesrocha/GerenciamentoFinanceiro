package com.fabiogomesrocha.dto;

import java.util.Set;

public class UserInfoDTO {
    public Long userId;
    public String username;
    public Set<String> roles;

    public UserInfoDTO(Long userId, String username, Set<String> roles) {
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }
}
