package com.ihrm.demo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * @ClassName
 * @Description
 * @Author He.Wang
 * @Date 2020/6/9 22:07
 * @Version 1.0
 **/

public class ParseJwtTest {
    /**
     * 解析token 字符串
     * @param args
     */
    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4ODgiLCJzdWIiOiLlsI_nmb0iLCJpYXQiOjE1OTE3MTIwOTAsImNvbXBhbnlJZCI6IjEyMzQ1NiIsImNvbXBhbnlOYW1lIjoi5bCP5piOIn0.4ylSWPA1tbxdLeqNWO8FD7MigT6YtmaVIKK0a5TH-Wo";
        Claims claims = Jwts.parser().setSigningKey("ihrm").parseClaimsJws(token).getBody();

        //私有数据存在claims

        System.out.println(claims.getId());

        System.out.println(claims.getSubject());

        System.out.println(claims.getIssuedAt());

        //解析自定义claims
        String companyId = (String)claims.get("companyId");
        System.out.println(companyId);

        String companyName = (String)claims.get("companyName");
        System.out.println(companyName);

    }
}