package com.smartprep.smart_interview_platform.Repository;

import com.smartprep.smart_interview_platform.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
