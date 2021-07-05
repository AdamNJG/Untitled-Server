package com.untitled.server.service;

import com.untitled.server.configuration.JwtUtils;
import com.untitled.server.domain.auth.RefreshToken;
import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.auth.UserSettings;
import com.untitled.server.model.content.UserDTO;
import com.untitled.server.model.requests.LoginRequest;
import com.untitled.server.repository.auth.UserRepository;
import com.untitled.server.repository.auth.UserSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final String TOKEN ="token";
    private final String REFRESH = "refresh";

    @Value("${app.jwtRefreshExpirationMs}")
    private int REFRESH_MAX_AGE;

    @Value("${app.jwtExpirationMs}")
    private int TOKEN_MAX_AGE;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    UserSettingsRepository userSettingsRepository;


    public boolean checkValidDate(User passedUser){
        if(plusTwoWeeks(passedUser.getDateRegistered()).after(new Date())){
            return true;
        }
        return false;
    }



    public Date plusTwoWeeks(Date userCreated){
        Calendar c = Calendar.getInstance();
        c.setTime(userCreated);
        c.add(Calendar.DATE, 14);
        return c.getTime();
    }

    public ResponseEntity<?> passwordMismatch(User user){
        int count = user.getPwCount();
        count++;
        user.setPwCount(count);
        userRepository.save(user);
        int attempts = 5 - count;
        if(user.getPwCount() < 5){
        return ResponseEntity.ok("Incorrect password, " + attempts + " attempts left.");}
        else {
            user.setEnabled(false);
            user.setPwCount(0);
            userRepository.save(user);
            return ResponseEntity.ok("Too many attempts at password, user disabled, email sent to reset password and enable user.");
        }
    }

    public void resetCount(User user){
        user.setPwCount(0);
        userRepository.save(user);
    }

    public ResponseEntity<String> login(User user, LoginRequest loginRequest, HttpServletResponse res) {



        String username = loginRequest.getUsername().toLowerCase();

        Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));


            SecurityContextHolder.getContext().setAuthentication(authentication);


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId(), loginRequest.isRememberMe());

        res.addCookie(createCookie(TOKEN, jwt, loginRequest.isRememberMe()));
        res.addCookie(createCookie(REFRESH, refreshToken.getToken(), loginRequest.isRememberMe()));

        return ResponseEntity.ok("Logged in");
    }

    public Cookie createCookie(String name, String token, boolean isRememberMe) {
        Cookie cookie = new Cookie(name, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        if (isRememberMe && name.contains(TOKEN)) {
            cookie.setMaxAge(TOKEN_MAX_AGE);
        } else if (isRememberMe && name.contains(REFRESH)) {
            cookie.setMaxAge(REFRESH_MAX_AGE);}
        return cookie;
    }

    public UserDTO sendUser(User user){

        UserSettings userSettings = getUserSettings(user);

        UserDTO userDTO = new UserDTO(user);
        userDTO.setUserSettings(userSettings);
        userDTO.setValidated(user.isValidated());
        return userDTO;
    }

    public UserSettings getUserSettings(User user){
        UserSettings userSettings = userSettingsRepository.findByUser(user);
        if (!(userSettings instanceof UserSettings)) {
            userSettings = new UserSettings(user);
        }
        return userSettings;
    }

}
