package com.cumulus.backend.activity.repository;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.club.domain.Club;
import com.cumulus.backend.club.domain.ClubMember;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityRepository {

    private final EntityManager em;

    public Activity save(Activity activity){
        if(activity.getId()==null){
            em.persist(activity);
            return activity;
        } else {
            return em.merge(activity);
        }
    }

    public Optional<Activity> findOne(Long activityId) {
        return Optional.ofNullable(em.find(Activity.class, activityId));
    }

    public void delete(Activity activity) {
        if(!em.contains(activity)){
            activity = em.merge(activity); // 영속상태가 아니면 병합
        }
        em.remove(activity);
    }

    public List<Activity> search(String sort) {
        if ("popular".equals(sort)) {
            return em.createQuery("""
                select a from Activity a
                left join a.activityLikes al
                group by a
                order by count(al) desc
            """, Activity.class).getResultList();
        } else if ("latest".equals(sort)) {
            return em.createQuery("""
                select a from Activity a
                order by a.createdAt desc
            """, Activity.class).getResultList();
        } else {
            return em.createQuery("select a from Activity a", Activity.class).getResultList();
        }
    }

    public List<Activity> findByHostingUser(ClubMember clubMember) {
        return em.createQuery("select a from Activity a where a.hostingUser =: hostingUser", Activity.class)
                .setParameter("hostingUser", clubMember)
                .getResultList();
    }

    public List<Activity> findByClub(Club club) {
        return em.createQuery("select a from Activity a where a.club = :club", Activity.class)
                .setParameter("club", club)
                .getResultList();
    }
}