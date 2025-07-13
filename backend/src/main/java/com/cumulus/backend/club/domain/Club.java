package com.cumulus.backend.club.domain;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.Category;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_id")
    private Long id;

    private String clubName;

    @Column(columnDefinition = "TEXT")
    private String clubDesc;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Campus campus;

    // 동아리 관리자
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_member_id", nullable = false)
    private ClubMember leader;

    // 동아리 참여자들
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> members = new ArrayList<>();

    // 동아리 모임들
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Activity> activities = new ArrayList<>();

    // 동아리 가입신청내역들
    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubApplication> applications = new ArrayList<>();
}
