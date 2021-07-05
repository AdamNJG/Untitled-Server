package com.untitled.server.repository.auth;

import com.untitled.server.domain.auth.EmailValToken;
import com.untitled.server.domain.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;


    public interface EmailTokenRepository extends JpaRepository<EmailValToken, Long> {

        EmailValToken findByToken(int token);

        int deleteByUser(User user);

        EmailValToken findByUser(User user);
}
