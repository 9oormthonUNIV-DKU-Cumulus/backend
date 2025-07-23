package com.cumulus.backend.club.repository;

import com.cumulus.backend.club.domain.Campus;
import com.cumulus.backend.club.domain.Category;
import com.cumulus.backend.club.domain.Club;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public List<Club> search(Category category, Campus campus, String sort) {
        StringBuilder jpql = new StringBuilder("select c from Club c ");

        if ("popular".equals(sort)) {
            jpql.append("left join c.members m ");
        }
        jpql.append("where c.category = :category and c.campus = :campus ");

        switch (sort) {
            case "popular" -> jpql.append("group by c.id order by count(m) desc");
            case "latest" -> jpql.append("order by c.createdAt desc");
            default -> {}
        }

        TypedQuery<Club> query = em.createQuery(jpql.toString(), Club.class)
                .setParameter("category", category)
                .setParameter("campus", campus);

        return query.getResultList();
    }

    public void delete(Club club) {
        if(!em.contains(club)){
            club = em.merge(club); // 영속상태가 아니면 병합
        }
        em.remove(club);
    }
}
