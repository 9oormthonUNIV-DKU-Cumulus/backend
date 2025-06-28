package com.cumulus.backend.activity.repository;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ActivityApplicationRepository {

    private final EntityManager em;

    public ActivityApplication save(ActivityApplication activityApplication){
        if(activityApplication.getId()==null){
            em.persist(activityApplication);
            return activityApplication;
        } else {
            return em.merge(activityApplication);
        }
    }

    public Optional<ActivityApplication> findOne(Long activityApplicationId){
        return Optional.ofNullable(em.find(ActivityApplication.class, activityApplicationId));
    }

    public List<ActivityApplication> findByActivity(Activity activity){
        return em.createQuery(
                "select a from ActivityApplication a where a.activity =: activity", ActivityApplication.class)
                .setParameter("activity", activity)
                .getResultList();
    }
}
