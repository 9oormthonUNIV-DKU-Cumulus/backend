package com.cumulus.backend.activity.repository;

import com.cumulus.backend.activity.domain.Activity;
import com.cumulus.backend.activity.dto.ActivitySearchRequestDto;
import com.cumulus.backend.common.Category;
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

    public List<Activity> search(ActivitySearchRequestDto activitySearchRequestDto, Category category) {
        //필터조건
        String jpql = "select a from Activity a where a.category = :category";

        // 정렬조건
        if( "latest".equals(activitySearchRequestDto.getSort()) ) {
            jpql += " order by a.createdAt desc";
        } else if( "popular".equals(activitySearchRequestDto.getSort()) ){
            jpql += " order by size(a.activityLikes) desc";
        }

        var query = em.createQuery(jpql, Activity.class);
        query.setParameter("category", category);

        return query.getResultList();
    }
}