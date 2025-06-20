package com.cumulus.backend.like.domain;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "activity_like")
@Getter
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
}
