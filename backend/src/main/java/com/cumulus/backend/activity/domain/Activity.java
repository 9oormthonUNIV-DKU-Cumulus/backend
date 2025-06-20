package com.cumulus.backend.activity.domain;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.common.Category;
import com.cumulus.backend.like.domain.Like;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    private int maxParticipants;

    private int nowParticipants;

    private boolean isPrivate = false;

    @Enumerated(EnumType.STRING)
    private Category category;

    // 모임 개최자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User hostingUser;

    // 동아리 모임글일 경우
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id", nullable = true)
    private Club club;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    private List<Like> activityLikes = new ArrayList<>();
}
