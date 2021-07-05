package com.untitled.server.controller;

import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.untitled.server.configuration.JwtUtils;
import com.untitled.server.domain.auth.*;
import com.untitled.server.model.requests.*;
import com.untitled.server.model.response.MessageResponse;
import com.untitled.server.model.response.TokenRefreshResponse;
import com.untitled.server.repository.auth.*;
import com.untitled.server.service.PasswordTokenService;
import com.untitled.server.service.RefreshTokenService;
import com.untitled.server.service.EmailValService;
import com.untitled.server.service.exception.TokenRefreshException;
import com.untitled.server.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {


    private final String REFRESH = "refresh";


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    PasswordTokenService passwordTokenService;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;

    @Autowired
    UserSettingsRepository userSettingsRepository;

    @Autowired
    EmailValService emailValService;

    @Autowired
    EmailTokenRepository emailTokenRepository;

    @Autowired
    RefreshTokenService refreshTokenService;

    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse res) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if(user instanceof User) {
            if (user.isEnabled()) {
                if (user.isValidated()) {
                    if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        return authService.passwordMismatch(user);
                    } else {
                        authService.resetCount(user);
                    }

                    return authService.login(user, loginRequest, res);

                } else if (authService.checkValidDate(user)) {
                    if (!encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                        return authService.passwordMismatch(user);
                    } else {
                        authService.resetCount(user);
                    }
                    return authService.login(user, loginRequest, res);
                }
                try {
                    emailValService.sendToken(user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ResponseEntity.ok("User is not validated, please validate email, new validation email sent.");
            }
                passwordTokenService.sendToken(user);
            return ResponseEntity.ok("User is disabled, please reset password, new password reset email sent.");
        }
        return ResponseEntity.ok("User not found");
    }

    @PostMapping("/pwtoken")
    public ResponseEntity<?> supplyPasswordToken(@RequestBody PasswordReq pw){
        User user = null;
        PasswordToken pwToken = new PasswordToken();

        try{
            user = userRepository.findByUsername(pw.getUsername());
            if(user == null){
                user = userRepository.findByEmail(pw.getUsername());
            }
        }catch(Exception ex){ResponseEntity.ok("User not found");
        }

        try {
            passwordTokenService.saveToken(user, pwToken);
        } catch(Exception exception){
            return ResponseEntity.ok(exception.getMessage());
        }


        return ResponseEntity.ok("Password reset email sent");
    }


    @PutMapping("/resetpassword")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordReq pw){
        PasswordToken token = null;
        User user = null;
        try {
            token= passwordTokenRepository.findByToken(pw.getToken());

            if (token instanceof PasswordToken) {
                user = token.getUser();
            } else {
                return  ResponseEntity.ok("Invalid password token, please request a new token");
            }
        }catch(Exception e){ResponseEntity.ok(e);}

        try {
            user.setEnabled(true);
            user.setPassword(encoder.encode(pw.getNewPassword()));
            userRepository.save(user);
            passwordTokenRepository.delete(token);
        }catch(Exception exception){ResponseEntity.ok(exception);}

        return  ResponseEntity.ok("Password Reset");
    }

    @PostMapping("/emailval")
    public ResponseEntity<?> validateEmail(@RequestBody EmailValRequest val){
        EmailValToken valToken = null;
        User user = null;
        try {
            valToken = emailTokenRepository.findByToken(val.getToken());

            if(valToken instanceof EmailValToken){
                Date date = new Date();
                if(valToken.getExpiryDate().after(date)) {
                    user = valToken.getUser();
                } else { return ResponseEntity.ok(new MessageResponse("Validation link expired"));}
            } else {
                return ResponseEntity.ok(new MessageResponse("Invalid validation link"));
            }
        }catch(Exception e){return ResponseEntity.ok(e);}
        try{
                user.setValidated(true);
                userRepository.save(user);
        }catch(Exception ex){ return ResponseEntity.ok(ex);}
        emailTokenRepository.delete(valToken);
        return ResponseEntity.ok(new MessageResponse("Email Validated"));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshtoken(@Valid HttpServletRequest req, HttpServletResponse res) {

        Cookie cookies[] = req.getCookies();
        String requestRefreshToken = null;

        if(cookies != null && cookies.length > 0){
            for(Cookie c : cookies){
                if(c.getName().contains(REFRESH)){
                    requestRefreshToken = c.getValue();
                }
            }
        }

        return refreshTokenService.setToken(requestRefreshToken, res);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        String username = signUpRequest.getUsername().toLowerCase();
        if (userRepository.existsByUsername(username)) {
            return ResponseEntity
                    .ok("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .ok("Error: Email is already in use!");
        }


        // Create new user's account
        User user = new User(username,
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }


        user.setRoles(roles);
        userRepository.save(user);
        UserSettings userSettings = new UserSettings(user);
        userSettingsRepository.save(userSettings);

        try {
            emailValService.sendToken(user);
        } catch (Exception e){e.printStackTrace();}

        return ResponseEntity.ok("User registered successfully, validation email sent!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookies[] = request.getCookies();

        if(cookies != null && cookies.length > 0){
            for(Cookie c : cookies){
              c.setMaxAge(0);
              c.setPath("/");
              c.setHttpOnly(true);
              response.addCookie(c);
            }
        }
        return ResponseEntity.ok("logged out");
    }
}