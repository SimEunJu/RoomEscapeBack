package com.sej.escape.dto.member;

import com.sej.escape.entity.constants.SocialLogin;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.Map;

@Getter
@Setter
public class MemberDto extends User implements OAuth2User {

    private long id;
    private String email;
    private SocialLogin socialLogin;
    private String nickname;

    private Map<String, Object> oauthAttrs;

    public MemberDto(String username, String password,
                     Collection<? extends GrantedAuthority> authorities){
        super(username, password, authorities);
        email = username;
    }

    // Returns the name of the authenticated Principal ?
    public String getName(){
        return this.email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauthAttrs;
    }
}
