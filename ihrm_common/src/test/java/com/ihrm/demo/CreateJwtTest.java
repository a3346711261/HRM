package com.ihrm.demo;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/9 22:03
 * @Version 1.0
 **/

public class CreateJwtTest {

    /**
     * 通过jwt创建token
     * @param args
     */
    public static void main(String[] args) {
        JwtBuilder builder = Jwts.builder().setId("888").setSubject("小白")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256,"ihrm")
                .claim("companyId","123456")
                .claim("companyName","小明");
        String token = builder.compact();
        System.out.println(token);
    }
}