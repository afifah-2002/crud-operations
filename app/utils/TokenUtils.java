package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import models.User;
import play.mvc.Http;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import play.mvc.Http.Request;
import java.security.SecureRandom;
import java.util.Base64;


public class TokenUtils {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateJwtToken(User user) {
        // Define secret key for signing the JWT token
        //Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Generate JWT token with user's email as subject and role as a claim
        String token = Jwts.builder()
                .setSubject(String.valueOf(user.getId()))
                .claim("role", user.getRole())
                .signWith(key)
                .compact();

        return token;
    }



    public static String extractUserRoleFromToken(Http.Request request) throws Exception {
        // Extract and validate JWT token from request headers
        String token = extractTokenFromRequest(request);

        // Validate and decode JWT token to get user role
        String userRole = decodeUserRoleFromToken(token, key);

        return userRole;
    }



    private static String decodeUserRoleFromToken(String token, Key key) throws Exception {
        try {
            // Parse the JWT token
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)  // Set your secret key here
                    .build()
                    .parseClaimsJws(token);

            //Jws<Claims> claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token);

            // Extract the user role claim from the parsed token
            String userRole = claims.getBody().get("role", String.class);

            if (userRole == null || userRole.isEmpty()) {
                throw new Exception("User role not found in JWT token");
            }

            return userRole;
        } catch (Exception e) {
            return ("Error decoding JWT token: " );
        }
    }



    public static String extractTokenFromRequest(Http.Request request) throws Exception {
        // Extract token from request headers
        String authorizationHeader = request.getHeaders().get("Authorization").orElse(null);

        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new Exception("Token not found in request headers");
        }

        // Extract the token part from the Authorization header
        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2 || !parts[0].equalsIgnoreCase("Bearer")) {
            throw new Exception("Invalid authorization header format");
        }
        String token = parts[1];

        return token;
    }

    public static String extractUserIdFromToken(String token) throws Exception {
        try {
            // Extract and validate user email from the token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Extract user email from token
        } catch (Exception e) {
            return("Error extracting user email from token");
        }
    }

}
