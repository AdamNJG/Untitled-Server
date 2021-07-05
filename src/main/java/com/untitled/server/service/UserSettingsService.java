package com.untitled.server.service;

import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.auth.UserSettings;
import com.untitled.server.model.content.UserDTO;
import com.untitled.server.repository.auth.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

    @Autowired
    UserSettingsRepository userSettingsRepository;

    public UserDTO attachSettings(User user){
        UserSettings userSettings = userSettingsRepository.findByUser(user);
        UserDTO userDTO = new UserDTO(user);
        userDTO.setUserSettings(userSettings);
        return userDTO;
    }
}
