package com.kglsys.infra.jwt;

import com.kglsys.infra.details.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // 用于签名 JWT 的密钥
    private final SecretKey secretKey;
    // JWT 的有效时长（毫秒）
    private final long jwtExpirationMs;
    // 角色信息在 JWT claim 中的 key
    private final String ROLES_CLAIM = "roles";

    // 构造方法：从配置中注入密钥和过期时间，生成签名密钥对象
    public JwtProvider(@Value("${kglsys.jwt.secret}") String secret,
                       @Value("${kglsys.jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.jwtExpirationMs = expirationMs;
    }

    /**
     * 生成 JWT token
     */
    public String generateToken(Authentication authentication) {
        // 1. 获取当前用户的认证信息（已登录用户）
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        // 2. 记录当前时间和过期时间
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // 3. 获取用户的角色信息（权限列表）
        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // 4. 构建 JWT，包括以下 claim 信息：
        // - subject（用户名）
        // - roles（用户角色）
        // - userId（用户 ID）
        // - 签发时间和过期时间
        // - 使用 HS512 算法和密钥签名
        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .add("alg", "HS512")
                .and()
                .subject(userPrincipal.getUsername())
                .claim(ROLES_CLAIM, roles)
                .claim("userId", userPrincipal.getId())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(secretKey, Jwts.SIG.HS512)
                .compact(); // 返回压缩后的 JWT 字符串
    }

    /**
     * 从 token 中解析出用户名
     */
    public String getUsernameFromToken(String token) {
        // 1. 使用签名密钥解析 token 并获取其 claims 内容
        // 2. 从 claims 中获取 subject，即用户名
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * 验证 token 的合法性
     */
    public boolean validateToken(String authToken) {
        try {
            // 1. 使用密钥验证 token 的签名是否正确，格式是否符合预期
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(authToken); // 如果解析成功，token 是合法的
            return true;
        } catch (SignatureException ex) {
            // 签名不合法（可能被篡改）
            logger.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            // 格式错误（不符合 JWT 格式）
            logger.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            // 已过期
            logger.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            // 使用了不支持的 JWT 特性
            logger.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            // 空字符串、空 claims 等非法情况
            logger.error("JWT claims string is empty: {}", ex.getMessage());
        }
        // token 验证失败
        return false;
    }
}