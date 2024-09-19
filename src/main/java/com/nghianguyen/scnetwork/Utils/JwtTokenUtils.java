package com.nghianguyen.scnetwork.Utils;

import com.nghianguyen.scnetwork.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(User user) throws Exception{
//        this.generateSecretKey();
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("userId", user.getId());
        try {
            String token = Jwts
                    .builder()
                    .setClaims(claims)
                    .setSubject(user.getEmail())
                    .signWith(getSignKey(), SignatureAlgorithm.HS256)
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                    .compact();
            return token;
        }
        catch (Exception e){
            throw new InvalidParameterException("Cannot create jwt token error: " + e.getMessage());
        }
    }

   private Key getSignKey(){
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Wdif6reUEVYXjIw8J9xGsIUBWy8tKXqaYYSQotiJfMo=
        return Keys.hmacShaKeyFor(bytes);
   }

   private String generateSecretKey() {
       SecureRandom random = new SecureRandom();
       byte[] keyBytes = new byte[32];
       random.nextBytes(keyBytes);
       String secretKey = Encoders.BASE64.encode(keyBytes);
       return secretKey;
   }

   private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()).build()
                .parseClaimsJws(token).getBody();
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claim = extractAllClaims(token);
        return claimsResolver.apply(claim);
   }

   public boolean isTokenExpired(String token) {
        Date tokenExpiration = this.extractClaim(token, Claims::getExpiration);
        return tokenExpiration.before(new Date());
   }
   public String extractEmail(String token){
        return this.extractClaim(token, Claims::getSubject);
   }

}
