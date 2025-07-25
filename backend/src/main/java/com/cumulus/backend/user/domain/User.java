package com.cumulus.backend.user.domain;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubApplication;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.like.domain.Like;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "dmatch_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;    // 암호화된 비밀번호값

    @Enumerated(EnumType.STRING)
    private Role role;

    private String major;

    private String phoneNumber;

    private String profileImageUrl;

    // 신청한 동아리
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ClubApplication> applications = new ArrayList<>();

    // 좋아요한 모임
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Like> activityLikes = new ArrayList<>();
}
