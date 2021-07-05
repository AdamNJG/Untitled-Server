package com.untitled.server.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import com.untitled.server.configuration.JwtUtils;
import com.untitled.server.domain.auth.RefreshToken;
import com.untitled.server.domain.auth.User;
import com.untitled.server.model.response.TokenRefreshResponse;
import com.untitled.server.repository.auth.RefreshTokenRepository;
import com.untitled.server.repository.auth.UserRepository;
import com.untitled.server.service.exception.TokenRefreshException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;


@Service
public class RefreshTokenService {

    private final String TOKEN ="token";
    private final String REFRESH = "refresh";

    @Value("${app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;


    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthService authService;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId, boolean remember) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setRememberMe(remember);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please sign in again");
        }

        return token;
    }


    @Transactional
    public int deleteByUserId(Long userId) {
        return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
    }

    public ResponseEntity<?> setToken(String rToken, HttpServletResponse res){

        RefreshToken refreshToken;
        try {
           refreshToken = verifyExpiration(findByToken(rToken).orElseThrow(() -> new TokenRefreshException(rToken, "Refresh token is not in database!")));
           User user = refreshToken.getUser();
           String jwtToken = jwtUtils.generateTokenFromUsername(user.getUsername());
           res.addCookie(authService.createCookie(TOKEN, jwtToken, refreshToken.isRememberMe()));

        } catch(Exception e){}

        return ResponseEntity.ok("Cookie refreshed");

    }
}