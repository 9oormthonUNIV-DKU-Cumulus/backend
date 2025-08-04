package com.cumulus.backend.like.repository;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.like.domain.Like;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LikeRepository {
    private final EntityManager em;

    public Like save(Like like) {
        if (like.getId() == null) {
            em.persist(like);
            return like;
        } else {
            return em.merge(like);
        }
    }

    public boolean existsByUserAndClub(User user, Club club) {
        String jpql = "SELECT count(l) > 0 FROM Like l WHERE l.user = :user AND l.club = :club";
        return em.createQuery(jpql, Boolean.class)
                .setParameter("user", user)
                .setParameter("club", club)
                .getSingleResult();
    }
}
