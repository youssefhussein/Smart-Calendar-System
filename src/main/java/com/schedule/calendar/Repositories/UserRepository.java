package com.schedule.calendar.Repositories;

import com.schedule.calendar.Models.Organization;
import com.schedule.calendar.Models.Organization;
import com.schedule.calendar.Models.User;
import com.schedule.calendar.Models.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

   Optional<User> findByUsername(String username);

   Optional<User> findByEmail(String email);

   List<User> findByOrganization(Organization organization);

   List<User> findByOrganizationAndUserType(Organization organization, UserType userType);
}
