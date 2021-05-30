package com.sej.escape.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sej.escape.dto.member.MemberDto;
import com.sej.escape.dto.member.MemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OauthAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${front.url.base}")
    private String FRONT_BASE_URL;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        super.setDefaultTargetUrl(FRONT_BASE_URL);

        MemberDto memberDto = (MemberDto) authentication.getPrincipal();
        // role 1개만 가정
        List<String> roles = memberDto.getAuthorities().stream().map(auth -> auth.getAuthority().substring(5)).collect(Collectors.toList());

        MemberRes payload = MemberRes.builder()
                .id(memberDto.getId())
                .nickname(memberDto.getNickname())
                .email(memberDto.getEmail())
                .role(roles.get(0))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String payloadString = mapper.writeValueAsString(payload);
        String payloadEncoded = Base64.getEncoder().encodeToString(payloadString.getBytes("utf-8"));

        Cookie cookie = new Cookie("token", payloadEncoded);
        response.addCookie(cookie);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
