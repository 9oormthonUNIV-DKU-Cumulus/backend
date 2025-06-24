package com.cumulus.backend.user.repository;

import com.cumulus.backend.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public User save(User user){
        if(user.getId()==null){
            em.persist(user);
            return user;
        } else {
            return em.merge(user);
        }
    }

    public Optional<User> findOne(Long id){ return Optional.ofNullable(em.find(User.class, id)); }

    public Optional<User> findByEmail(String email) {
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.email = :email", User.class
                    )
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e){
            return Optional.empty();
        }
    }

    public boolean existsByEmail(String email){
        Long count = em.createQuery(
                        "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class
                )
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }
}
