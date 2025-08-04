package com.cumulus.backend.like.domain;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.exception.CustomException;
import com.cumulus.backend.exception.ErrorCode;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dmatch_like")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id")
    private Activity activity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    public void validateTarget() {
        if ((club == null && activity == null) || (club != null && activity != null)) {
            throw new IllegalStateException("Like는 club 또는 activity 중 하나에만 연결되어야 합니다.");
        }
    }
}
