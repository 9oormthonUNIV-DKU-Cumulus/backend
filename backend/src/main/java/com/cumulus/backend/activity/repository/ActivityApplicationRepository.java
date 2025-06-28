package com.cumulus.backend.activity.repository;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.domain.ActivityApplication;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
