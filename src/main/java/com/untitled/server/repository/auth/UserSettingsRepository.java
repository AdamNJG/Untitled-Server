package com.untitled.server.repository.auth;

import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.auth.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

    UserSettings findByUser(User user);
}
