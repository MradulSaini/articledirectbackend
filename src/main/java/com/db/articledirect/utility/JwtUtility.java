package com.db.articledirect.utility;

import com.db.articledirect.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility implements Serializable {

    private static final long serialVersionUID = 234234523523L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;


    private String secretKey = "DB_HACK@12";

    //retrieve username from jwt token
    public Integer getUserIdFromToken(String token) {
        try {
            Map<String, Object> userDetails = (Map<String, Object>) getClaimFromToken(token, claims -> claims.get("userDetails"));
            if (userDetails.get("id") != null) {
                return (Integer) userDetails.get("id");
            }
        } catch (Exception ex){
            System.out.println("Exception while processing token");
            System.out.println(ex);
        }
        return -1;
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }


    //for retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    //generate token for user
    public String generateToken(User user) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getName());
        userMap.put("role", user.getRole());

        Map<String, Object> claims = new HashMap<>();
        claims.put("userDetails", userMap);
        String token = doGenerateToken(claims, user.getName());
        return token;
    }


    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(CommonFunctions.getCurrentDate())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }


    //validate token
    public Boolean validateToken(String token, User userDetails) {
//        final String username = getUserIdFromToken(token);
        Boolean isValid = false;
        if(!isTokenExpired(token)) {
            Map<String, Object> userDetailsFromToken = (Map<String, Object>) getClaimFromToken(token, claims -> claims.get("userDetails"));
            String nameFromToken = (String) userDetailsFromToken.get("name");
            int role = (int) userDetailsFromToken.get("role");
            String email = (String) userDetailsFromToken.get("email");

            if(userDetails.getName().equals(nameFromToken) && userDetails.getEmail().equals(email) && userDetails.getRole() == role){
                isValid = true;
            }
        }
        return isValid;
    }
    public User getUserDetailsFromToken(String token){
        Map<String, Object> userDetails = (Map<String, Object>) getClaimFromToken(token, claims -> claims.get("userDetails"));

        User u = new User();
        u.setEmail((String) userDetails.get("email"));
        u.setRole((int) userDetails.get("role"));
        System.out.println(u);

        return u;
    }
}