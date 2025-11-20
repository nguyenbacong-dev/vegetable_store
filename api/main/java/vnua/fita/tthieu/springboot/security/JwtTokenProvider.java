package vnua.fita.tthieu.springboot.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long JWT_EXPIRATION = 900000L; // 15 phút

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtSecret) // hoặc Keys.hmacShaKeyFor(JWT_SECRET.getBytes()) nếu bạn dùng String key
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            System.out.println("Invalid JWT token: " + ex.getMessage());
            return false;
        }
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtSecret) // giống như trên
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
