package com.example.resourceserver.config;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.ArrayList;
import java.util.List;

public class TokenGroupsConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    @Override
    public AbstractAuthenticationToken convert(Jwt source) {
        JwtAuthenticationConverter defaultConverter = new JwtAuthenticationConverter();
        AbstractAuthenticationToken token = defaultConverter.convert(source);
        Object realmroles = source.getClaims().get("realm_access");
        if (realmroles instanceof JSONObject) {
            JSONArray roles = (JSONArray) ((JSONObject)realmroles).get("roles");
            List<GrantedAuthority> authorities = new ArrayList<>(token.getAuthorities());
            roles.stream().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            });
            return new JwtAuthenticationToken(source, authorities);
        }
        return token;
    }
}
