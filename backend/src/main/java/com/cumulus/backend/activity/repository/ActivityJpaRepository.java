package com.cumulus.backend.activity.repository;

import com.cumulus.backend.activity.domain.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityJpaRepository extends JpaRepository<Activity, Long> {
}