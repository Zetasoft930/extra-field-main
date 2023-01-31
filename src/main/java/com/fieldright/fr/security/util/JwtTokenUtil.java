package com.fieldright.fr.security.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta classe terá métodos para nos auxiliar na criação e tratamento de tokens (manipular tokens)
 */
@Component
public class JwtTokenUtil {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    /**
     * Importando algumas variáveis do "aplication.properties"
     */
    @Value("jwt.secret")
    private String secret;
    @Value("${jwt.expiration}")
    private Long expiration;

    public String recuperaEmailPelo(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String recuperaEmailPelo(HttpServletRequest request) {
        return recuperaEmailPelo(recuperaTokenDo(request));
    }

    @Nullable
    public String recuperaTokenDo(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER); //o token vem no campo "Autorisation" do header da requisição

        if (token != null && token.startsWith(BEARER_PREFIX)) {
            token = token.substring(7);//recuperar o token tirando o "Bearer " da frente
        }
        return token;
    }

    public boolean validToken(String token) {
        return !expiredToken(token);
    }

    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean expiredToken(String token) {
        Date expirationDate = this.getExpirationDateFromToken(token);

        if (expirationDate == null) {
            return false;
        }
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    public String getToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());

        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }
}
