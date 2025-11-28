package ro.msg.learning.shop.util;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Jwts;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.config.RsaConfigProperties;

import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {
    private final int EXPIRATION_TIME = 3600000;
    private RsaConfigProperties rsaConfigProperties;

    public String generateToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", authentication.getAuthorities())
                .setIssuer("self")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(rsaConfigProperties.privateKey(), SignatureAlgorithm.RS256)
                .compact();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(rsaConfigProperties.publicKey()).build();
    }
}
