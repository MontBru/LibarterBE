package com.bryan.libarterbe.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtUtility {
    public static Jwt getJwt()
    {
        return (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public static int getUid()
    {
        Jwt jwt = getJwt();
        return Math.toIntExact(jwt.getClaim("uid"));
    }
}
