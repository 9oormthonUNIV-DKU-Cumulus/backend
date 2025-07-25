package com.cumulus.backend.club.domain;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_member_id")
    private Long id;

    private String memberName;
    private String profileImgUrl;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    // 대상유저
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 소속 동아리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    // 개설한, 관리중인 모임(nullable)
    @OneToMany(mappedBy = "hostingUser")
    private List<Activity> hostingActivities = new ArrayList<>();

    // 신청한 모임(nullable)
    @OneToMany(mappedBy = "applicant")
    private List<ActivityApplication> activityApplications  = new ArrayList<>();
}
