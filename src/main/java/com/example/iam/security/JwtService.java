package com.example.iam.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * Service for handling JWT token operations.
 */
@Service
@RequiredArgsConstructor
public final class JwtService {

  /**
   * Number of milliseconds in a second.
   */
  private static final int MILLISECONDS_IN_SECOND = 1000;

  /**
   * Secret key for JWT signing.
   */
  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  /**
   * Access token expiration time in minutes.
   */
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;

  /**
   * Refresh token expiration time in minutes.
   */
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  /**
   * Extracts username from JWT token.
   *
   * @param token the JWT token
   * @return the username
   */
  public String extractUsername(final String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Generates access token for user.
   *
   * @param userDetails the user details
   * @return the JWT token
   */
  public String generateAccessToken(final UserDetails userDetails) {
    return generateAccessToken(new HashMap<>(), userDetails);
  }

  /**
   * Generates access token with additional claims.
   *
   * @param extraClaims additional claims to include
   * @param userDetails the user details
   * @return the JWT token
   */
  public String generateAccessToken(
      final Map<String, Object> extraClaims,
      final UserDetails userDetails) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  /**
   * Generates refresh token for user.
   *
   * @param userDetails the user details
   * @return the refresh token
   */
  public String generateRefreshToken(final UserDetails userDetails) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(
      final Map<String, Object> extraClaims,
      final UserDetails userDetails,
      final long expiration) {
    return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis()
            + expiration * MILLISECONDS_IN_SECOND))
        .signWith(getSignInKey())
        .compact();
  }

  /**
   * Validates JWT token.
   *
   * @param token       the JWT token
   * @param userDetails the user details
   * @return true if token is valid
   */
  public boolean isTokenValid(final String token, final UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(final String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(final String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Extracts a claim from JWT token.
   *
   * @param token          the JWT token
   * @param claimsResolver function to extract specific claim
   * @param <T>            type of the claim
   * @return the extracted claim
   */
  public <T> T extractClaim(
      final String token,
      final Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(final String token) {
    return Jwts.parser()
        .verifyWith(getSignInKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private SecretKey getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
