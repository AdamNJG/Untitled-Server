package com.untitled.server.service;

import com.untitled.server.domain.auth.EmailValToken;
import com.untitled.server.domain.auth.User;
import com.untitled.server.repository.auth.EmailTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailValService {

    @Autowired
    EmailTokenRepository emailTokenRepository;

    @Autowired
    EmailService emailService;

    public int generateToken() {

        int max = 999999;
        int min = 100000;
        int randomInt = (int)Math.floor(Math.random()*(max-min+1)+min);

        int token = randomInt;

        return token;
    }

    public void sendToken(User user) throws Exception{
        if(user instanceof User) {

            EmailValToken oldToken = emailTokenRepository.findByUser(user);

            if(oldToken instanceof EmailValToken){
                emailTokenRepository.delete(oldToken);
            }

            EmailValToken rToken = new EmailValToken();
            rToken.setUser(user);
            rToken.setToken(generateToken());

            try {
                emailTokenRepository.save(rToken);
            } catch (Exception ex) {
                try {
                    rToken.setToken(generateToken());
                    emailTokenRepository.save(rToken);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

            String token = String.valueOf(rToken.getToken());

            emailService.sendRegEmail(user, token);

        } else {throw new Exception("User not found");}
    }

}
