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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, columnDefinition = "varchar(1000)", nullable = false, updatable = false)
    String email;

    @Column(length = 1000)
    private String password;

    @Column(nullable = false, length = 100)
    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;

    @Builder.Default
    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private Set<MemberRole> roles = new HashSet<>();

    @Column(length = 100)
    private String memberName;

    @Column(length = 200)
    private String nickname;

    // 정지회원 관리
    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean unable;

    @Column(columnDefinition = "text")
    private String unableReason;

    private LocalDateTime unableDate;

    private LocalDateTime leaveDate;

}
