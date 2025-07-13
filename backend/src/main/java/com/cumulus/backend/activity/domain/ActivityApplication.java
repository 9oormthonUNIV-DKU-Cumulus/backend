package com.cumulus.backend.activity.domain;

import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.domain.ApplyStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ActivityApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_member_id", nullable = false)
    private ClubMember applicant;

    @Column(columnDefinition = "TEXT")
    private String applicationText;

    private LocalDateTime createdAt;

    private String applyUserName;

    private String applyUserPhoneNumber;

    private String applyUserMajor;
}
