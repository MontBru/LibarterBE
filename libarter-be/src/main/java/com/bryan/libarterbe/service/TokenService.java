package com.bryan.libarterbe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {
    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    public JwtDecoder jwtDecoder;

    public String generateJwt(Authentication auth, int id)
    {
        Instant now = Instant.now();

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("libarter")
                .issuedAt(now)
                .subject(auth.getName())
                .claim("uid", id)
                .claim("roles",scope)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
