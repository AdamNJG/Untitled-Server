package com.untitled.server.controller;

import com.untitled.server.configuration.JwtUtils;
import com.untitled.server.domain.auth.User;
import com.untitled.server.domain.content.Post;
import com.untitled.server.model.content.PostDTO;
import com.untitled.server.model.content.UserDTO;
import com.untitled.server.repository.auth.UserRepository;
import com.untitled.server.repository.content.PostRepository;
import com.untitled.server.service.AuthService;
import com.untitled.server.service.EmailService;
import com.untitled.server.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/content")
public class ContentController {


    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthService authService;



    @PostMapping("/getuserdetails")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserDTO getUserDetails( @RequestBody String username){
        User user = userRepository.findByUsername(username);
        return authService.sendUser(user);
    }

    @GetMapping("/getuser")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserDTO getuser(HttpServletRequest req) {
        Cookie cookies[] = req.getCookies();
        String token = null;

        if(cookies != null && cookies.length > 0){
            for(Cookie c : cookies){
                if(c.getName().contains("token")){
                    token = c.getValue();
                }
            }
        }

        String username = jwtUtils.getUserNameFromJwtToken(token);

        User user = userRepository.findByUsername(username);

        return authService.sendUser(user);
    }


}