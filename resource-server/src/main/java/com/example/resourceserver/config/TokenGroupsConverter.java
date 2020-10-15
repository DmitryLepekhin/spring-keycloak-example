package com.example.resourceserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TokenGroupsConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        JwtAuthenticationConverter defaultConverter = new JwtAuthenticationConverter();
        AbstractAuthenticationToken token = defaultConverter.convert(source);
        Object realmroles = source.getClaims().get("realmroles");
        if (realmroles instanceof Collection) {
            List<GrantedAuthority> authorities = new ArrayList<>(token.getAuthorities());
            ((Collection<?>) realmroles).stream().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            });
            return new JwtAuthenticationToken(source, authorities);
        }
        return token;
    }
}
