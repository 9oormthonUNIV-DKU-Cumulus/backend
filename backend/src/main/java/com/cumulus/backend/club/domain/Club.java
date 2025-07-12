package com.cumulus.backend.club.domain;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User leader;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubApplication> applications = new ArrayList<>();

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<ClubMember> members = new ArrayList<>();
}
