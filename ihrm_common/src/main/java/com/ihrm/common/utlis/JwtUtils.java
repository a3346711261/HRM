package com.ihrm.common.utlis;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.Map;

/**
 * @ClassName Jwt工具类
 * @Description
 * @Author He.Wang
 * @Date 2020/6/9 22:16
 * @Version 1.0
 **/

@Data
//@ConfigurationProperties(prefix = "jwt.config")
@Configuration
public class JwtUtils {
    /**
     * 签名的私钥
     */
    private String key;

    /**
     * 签名的实效时间
     */
    private Long ttl;

    /**
     * 设置认证token
     *      id:登录用户id
     *      subject:登录用户名
     *
     */
    public String createJwt(String id, String name, Map<String,Object> map){
        //1.设置失效时间
        long now = System.currentTimeMillis();  //获取当前毫秒
        long exp = now + ttl;

        //2.创建JwtBuilder
        JwtBuilder jwtBuilder = Jwts.builder().setId(id).setSubject(name)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,key);
        //3.根据map设置claim
        for (Map.Entry<String,Object> entry : map.entrySet()){
            jwtBuilder.claim(entry.getKey(),entry.getValue());
        }
        jwtBuilder.setExpiration(new Date(exp));
        //创建token
        String token = jwtBuilder.compact();
        return token;
    }

    /**
     * 解析token字符串获取clamis
     */
    public Claims parseJwt(String token){
        Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        return claims;
    }
}