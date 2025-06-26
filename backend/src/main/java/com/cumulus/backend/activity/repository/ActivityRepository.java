package com.cumulus.backend.activity.repository;

import com.cumulus.backend.activity.domain.Activity;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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

}
