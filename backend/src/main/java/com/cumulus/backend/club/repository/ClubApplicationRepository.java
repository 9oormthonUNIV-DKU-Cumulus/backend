package com.cumulus.backend.club.repository;

import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubApplication;
import com.cumulus.backend.user.domain.User;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public Optional<ClubApplication> findOne(Long applicationId) {
        return Optional.ofNullable(em.find(ClubApplication.class, applicationId));
    }

    public void delete(ClubApplication clubApplication) {
        if (!em.contains(clubApplication)) {
            clubApplication = em.merge(clubApplication);
        }
        em.remove(clubApplication);
    }

    public List<ClubApplication> findByApplicant(User user) {
        return em.createQuery("select ca from ClubApplication ca where ca.user = :user", ClubApplication.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<ClubApplication> findByClubs(List<Club> clubs) {
        return em.createQuery("select ca from ClubApplication ca where ca.club in :clubs and ca.status = 'PENDING'",
                        ClubApplication.class)
                .setParameter("clubs", clubs)
                .getResultList();
    }
}
