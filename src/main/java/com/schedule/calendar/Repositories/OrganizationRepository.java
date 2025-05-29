package com.schedule.calendar.Repositories;

import com.schedule.calendar.Models.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Organization findByName(String name);
    boolean existsByName(String name);
}