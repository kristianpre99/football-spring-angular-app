package it.kristianp.footballbackendwebapp.auth.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import it.kristianp.footballbackendwebapp.properties.FootballAppConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final static String SECRET_KEY = "2a4c4b635e486d364b62367e475344673527435d2566303f6b773c4d40";
    private static final SecretKey S_KEY = Jwts.SIG.HS256.key().build();

    private final FootballAppConfigProperties footballAppConfigProperties;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(null, userDetails);
    }


    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails) {
        Instant instant = Instant.now();
        Date issuedDate = Date.from(instant);
        Date expDate = Date.from(instant.plus(footballAppConfigProperties.getAuthenticationMinutesDuration(), ChronoUnit.MINUTES));
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(expDate)
                .signWith(S_KEY)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(S_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignIngKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
