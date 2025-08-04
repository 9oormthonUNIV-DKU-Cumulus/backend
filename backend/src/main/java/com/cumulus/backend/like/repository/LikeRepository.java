package com.cumulus.backend.like.repository;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.like.domain.Like;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    public Optional<Like> findByUserAndClub(Long userId, Long clubId) {
        String jpql = "SELECT like FROM Like like WHERE like.user.id = :userId AND like.club.id = :clubId";
        return em.createQuery(jpql, Like.class)
                .setParameter("userId", userId)
                .setParameter("clubId", clubId)
                .getResultList().stream().findFirst();
    }

    public boolean existsByUserIdAndClubId(Long userId, Long clubId) {
        String jpql = "SELECT count(l) > 0 FROM Like l WHERE l.user.id = :userId AND l.club.id = :clubId";
        return em.createQuery(jpql, Boolean.class)
                .setParameter("userId", userId)
                .setParameter("clubId", clubId)
                .getSingleResult();
    }

    public void delete(Like like) {
        if (!em.contains(like)) {
            like = em.merge(like);
        }
        em.remove(like);
    }

    public boolean existsByUserIdAndActivityId(Long userId, Long activityId) {
        String jpql = "SELECT count(l) > 0 FROM Like l WHERE l.user.id = :userId AND l.activity.id = :activityId";
        return em.createQuery(jpql, Boolean.class)
                .setParameter("userId", userId)
                .setParameter("activityId", activityId)
                .getSingleResult();
    }

    public Optional<Like> findByUserAndActivity(Long userId, Long activityId) {
        String jpql = "SELECT like FROM Like like WHERE like.user.id = :userId AND like.activity.id = :activityId";
        return em.createQuery(jpql, Like.class)
                .setParameter("userId", userId)
                .setParameter("activityId", activityId)
                .getResultList().stream().findFirst();
    }
}
