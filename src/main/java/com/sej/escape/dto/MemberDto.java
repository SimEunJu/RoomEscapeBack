package com.sej.escape.dto;

import com.sej.escape.entity.constants.SocialLogin;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
@Builder
public class MemberDto extends User implements OAuth2User
{

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
