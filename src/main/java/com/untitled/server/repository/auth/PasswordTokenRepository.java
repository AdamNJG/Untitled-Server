package com.untitled.server.repository.auth;

import com.untitled.server.domain.auth.PasswordToken;
import com.untitled.server.domain.auth.RefreshToken;
import com.untitled.server.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long>{

        PasswordToken findByToken(int token);

        int deleteByUser(User user);

        PasswordToken findByUser(User user);
    }
