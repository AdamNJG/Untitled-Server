package com.untitled.server.service;

import com.untitled.server.domain.auth.PasswordToken;
import com.untitled.server.domain.auth.User;
import com.untitled.server.repository.auth.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class PasswordTokenService {

    @Autowired
    PasswordTokenRepository passwordTokenRepository;


    @Autowired
    EmailService emailService;


    public int generateToken() {

        int max = 999999;
        int min = 100000;
        int randomInt = (int) Math.floor(Math.random() * (max - min + 1) + min);

        int token = randomInt;

        return token;
    }

    public void saveToken(User user, PasswordToken pwToken) throws Exception {
        if (user instanceof User) {

            PasswordToken oldToken = passwordTokenRepository.findByUser(user);

            if (oldToken instanceof PasswordToken) {
                passwordTokenRepository.delete(oldToken);
            }

            pwToken.setUser(user);
            pwToken.setToken(generateToken());
            try {
                passwordTokenRepository.save(pwToken);
            } catch (Exception ex) {
                try {
                    pwToken.setToken(generateToken());
                    passwordTokenRepository.save(pwToken);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            String token = String.valueOf(pwToken.getToken());

            emailService.sendPasswordEmail(user, token);


        } else {
            throw new Exception("User not found");
        }
    }

    public void sendToken(User user) {
        PasswordToken oldToken = passwordTokenRepository.findByUser(user);

        if (oldToken instanceof PasswordToken) {
            passwordTokenRepository.delete(oldToken);
        }
        PasswordToken pwToken = new PasswordToken();

        try {
            saveToken(user, pwToken);
        } catch (Exception exception) {
            exception.printStackTrace();
        }


    }
}
