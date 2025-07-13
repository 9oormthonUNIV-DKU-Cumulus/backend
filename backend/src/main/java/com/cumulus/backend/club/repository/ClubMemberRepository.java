package com.cumulus.backend.club.repository;

import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ClubMemberRepository {
    private final EntityManager em;

    public Optional<ClubMember> findByUserIdAndClubId(Long userId, Long clubId){
        List<ClubMember> resultList = em.createQuery(
                "select cm from ClubMember cm where cm.user.id = :userId and cm.club.id = :clubId")
                .setParameter("userId", userId)
                .setParameter("clubId", clubId)
                .getResultList();
        return resultList.stream().findFirst();
    };

    public List<ClubMember> findAllByUser(User user) {
        return em.createQuery("select cm from ClubMember cm where cm.user = :user", ClubMember.class)
                .setParameter("user", user)
                .getResultList();
    }
}
