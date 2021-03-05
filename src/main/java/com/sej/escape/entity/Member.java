package com.sej.escape.entity;

import com.sej.escape.entity.base.Base;
import com.sej.escape.entity.constants.MemberRole;
import com.sej.escape.entity.constants.SocialLogin;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member extends Base {

    @Id
    String email;

    private String password;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    @Column(length = 100)
    private String name;
    @Column(length = 200)
    private String nickname;

    // 정지회원 관리
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean unable;
    private String unableReason;
    private LocalDateTime unableDate;

    private LocalDateTime leaveDate;

}
