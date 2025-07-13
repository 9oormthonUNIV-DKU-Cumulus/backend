package com.cumulus.backend.activity.domain;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.like.domain.Like;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="activity_id")
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime meetingDate; // 모임일자

    private LocalDateTime deadline; // 신청마감일자

    private LocalDateTime createdAt; // 모임글 생성일자

    private int maxParticipants; // 최대참여 인원

    private int nowParticipants; // 현재참여 인원

    // 모임의 소속 동아리
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    // 모임 개최자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hosting_member_id", nullable = false)
    private ClubMember hostingUser;

    private boolean isPrivate = false;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<Like> activityLikes = new ArrayList<>();
}
