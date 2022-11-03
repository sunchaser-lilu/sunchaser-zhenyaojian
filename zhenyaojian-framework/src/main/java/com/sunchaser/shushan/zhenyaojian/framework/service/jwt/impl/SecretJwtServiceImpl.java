package com.sunchaser.shushan.zhenyaojian.framework.service.jwt.impl;

import com.sunchaser.shushan.zhenyaojian.framework.config.property.JwtProperties;
import com.sunchaser.shushan.zhenyaojian.framework.security.LoginUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

/**
 * jwt implementation based on secret encryption
 *
 * @author sunchaser admin@lilu.org.cn
 * @since JDK8 2022/11/3
 */
public class SecretJwtServiceImpl extends AbstractJwtService {

    public SecretJwtServiceImpl(JwtProperties jwtProperties) {
        super(jwtProperties);
    }

    /**
     * 创建 JWT
     *
     * @param user LoginUser
     * @return JWT
     */
    @Override
    public String createJwt(LoginUser user) {
        Claims claims = Jwts.claims();
        claims.setSubject(user.getUsername());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + jwtProperties.getExpiration()))
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 解析 JWT
     *
     * @param jwt JWT
     * @return Claims
     */
    @Override
    public Claims parseJwt(String jwt) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
    }
}
