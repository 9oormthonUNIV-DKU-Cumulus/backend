package com.cumulus.backend.activity.domain;

import com.cumulus.backend.common.ApplicationStatus;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ActivityApplication {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", nullable = false)
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String applicationText;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}
