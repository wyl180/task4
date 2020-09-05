package com.jnshu.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

/**
 * @ClassName JJWTUtil
 * @Description
 * @Author 韦延伦
 * @Date 2020/8/14 9:18
 * @Version 1.0
 */
public class JJWTUtil {
    /**
     * 生成jwt的方法
     * @param date 签发时间
     * @param secrety 私钥
     * @param
     * @return
     */
    public static String getJWT(String id,String userName,Date date,String secrety){

        JwtBuilder jwtBuilder= Jwts.builder().setIssuer(userName).setId(id)
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256,secrety);
        return jwtBuilder.compact();
    }

    /**
     * 解析token
     * @param token token
     * @param secrety 私钥
     * @return
     */
    public static Claims parseJWT(String token,String secrety){
        Claims claims=Jwts.parser().setSigningKey(secrety).parseClaimsJws(token).getBody();
        return claims;
    }
}
