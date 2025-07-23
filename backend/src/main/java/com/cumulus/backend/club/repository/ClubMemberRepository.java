package com.cumulus.backend.club.repository;

import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.club.domain.MemberRole;
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

    public ClubMember save(ClubMember member) {
        if(member.getId()==null){
            em.persist(member);
            return member;
        } else {
            return em.merge(member);
        }
    }

    public List<ClubMember> findAllByUser(User user) {
        return em.createQuery("select cm from ClubMember cm where cm.user = :user", ClubMember.class)
                .setParameter("user", user)
                .getResultList();
    }

    public Optional<ClubMember> findByUserIdAndClubId(Long userId, Long clubId){
        List<ClubMember> resultList = em.createQuery(
                "select cm from ClubMember cm where cm.user.id = :userId and cm.club.id = :clubId",
                        ClubMember.class)
                .setParameter("userId", userId)
                .setParameter("clubId", clubId)
                .getResultList();
        return resultList.stream().findFirst();
    }

    public Optional<ClubMember> findByClubIdAndRole(Long clubId, MemberRole role){
        List<ClubMember> resultList = em.createQuery(
                        "select cm from ClubMember cm where cm.club.id = :clubId and cm.role = :role",
                        ClubMember.class)
                .setParameter("clubId", clubId)
                .setParameter("role", role)
                .setMaxResults(1)
                .getResultList();
        return resultList.stream().findFirst();
    }
}
