package com.kglsys.infra.jwt.impl;

import com.kglsys.infra.details.CustomUserDetails;
import com.kglsys.infra.jwt.JwtTokenGenerator;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenGeneratorImpl implements JwtTokenGenerator {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenGeneratorImpl.class);

    // 用于签名 JWT 的密钥
    @Value("${kglsys.jwt.secret}")
    private String jwtSecretString;
    // JWT 的有效时长（毫秒）
    @Value("${kglsys.jwt.expiration-ms}")
    private long jwtExpirationMs;
    private SecretKey secretKey;
    // 角色信息在 JWT claim 中的 key
    private final String ROLES_CLAIM = "roles";

    /**
     * 【安全强化】: 使用 @PostConstruct 在依赖注入完成后初始化密钥。
     * 密钥应从环境变量或安全的配置中心获取，并进行 Base64 编码以增加安全性。
     * 此处假设配置文件中的密钥是 Base64 编码的。
     */
    @PostConstruct
    protected void init() {
        // 检查密钥长度，HMAC-SHA512 推荐使用至少 64 字节 (512位) 的密钥
        byte[] secretBytes = Base64.getDecoder().decode(jwtSecretString);
        if (secretBytes.length < 64) {
            logger.warn("Warning: The configured JWT secret is shorter than 64 bytes, which is not recommended for HS512.");
        }
        this.secretKey = Keys.hmacShaKeyFor(secretBytes);
    }

    /**
     * 生成 JWT token
     */
    @Override
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
     * 从 JWT 中解析出用户名。
     * @param token JWT 字符串。
     * @return 用户名。
     */
    @Override
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
     * 校验 JWT 的有效性。
     * @param authToken JWT 字符串。
     * @return 如果 token 有效则返回 true，否则返回 false。
     */
    @Override
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