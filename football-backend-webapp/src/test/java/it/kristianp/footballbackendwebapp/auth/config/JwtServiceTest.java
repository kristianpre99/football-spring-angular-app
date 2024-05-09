package it.kristianp.footballbackendwebapp.auth.config;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

class JwtServiceTest {

    @Test
    void test() {

        System.out.println(generateToken(null));
    }

    public String generateToken(Map<String, Object> extraClaims) {
        SecretKey secretKeySec = Jwts.SIG.HS256.key().build();

        Instant instant = Instant.now();
        Date issuedDate = Date.from(instant);
        Date expDate = Date.from(instant.plus(30, ChronoUnit.MINUTES));
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject("test")
                .issuedAt(issuedDate)
                .expiration(expDate)
//                .signWith(getSignIngKey(), SignatureAlgorithm.HS256)
                .signWith(secretKeySec)
                .compact();
    }

}