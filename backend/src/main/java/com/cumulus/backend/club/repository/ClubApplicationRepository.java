package com.cumulus.backend.club.repository;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubApplication;
import com.cumulus.backend.club.domain.ClubMember;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ClubApplicationRepository {
    private final EntityManager em;

    public boolean existsByClubAndApplicant(Club club, User applicant) {
        Long count = em.createQuery(
                        "select count(ca) from ClubApplication ca " +
                                "where ca.club = :club and ca.user = :applicant", Long.class)
                .setParameter("club", club)
                .setParameter("applicant", applicant)
                .getSingleResult();
        return count > 0;
    }

    public ClubApplication save(ClubApplication clubApplication) {
        if(clubApplication.getId()==null){
            em.persist(clubApplication);
            return clubApplication;
        } else {
            return em.merge(clubApplication);
        }
    }
}
