package com.cumulus.backend.club.repository;

import com.cumulus.backend.club.domain.Club;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClubRepository {
    private final EntityManager em;

    public Club save(Club club){
        if(club.getId()==null){
            em.persist(club);
            return club;
        } else {
            return em.merge(club);
        }
    }

    public Optional<Club> findOne(Long id){
        return Optional.ofNullable(em.find(Club.class, id));
    }
}
