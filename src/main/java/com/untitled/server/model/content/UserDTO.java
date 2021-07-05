package com.untitled.server.model.content;


import com.untitled.server.domain.auth.Role;
import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.auth.UserSettings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String role;

    private boolean validated;

    private UserSettings userSettings;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.validated = true;

        Set<Role> roles = user.getRoles();

        for(Role tempRole : roles){
            if(tempRole.getName().toString().contains("ROLE_MODERATOR")){
                this.role = "MOD";
                break;
            } else {
                this.role = "USER";
            }
        }
        this.role = role;
    }

    public UserDTO(String username) {
        this.username =username;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(UserSettings userSettings) {
        this.userSettings = userSettings;
    }
}
