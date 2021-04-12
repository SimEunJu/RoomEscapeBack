package com.sej.escape.dto.member;

import com.sej.escape.entity.constants.MemberRole;
import com.sej.escape.entity.constants.SocialLogin;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class MemberDto extends User implements OAuth2User {

    private long id;
    private String email;
    private SocialLogin socialLogin;
    private String name;
    private String nickname;

    private Map<String, Object> oauthAttrs;

    public MemberDto(String username, String password,
                     Collection<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
        email = username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthAttrs;
    }
}
