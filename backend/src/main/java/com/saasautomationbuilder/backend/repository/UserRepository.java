package com.saasautomationbuilder.backend.repository;

import com.saasautomationbuilder.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Example custom query methods (can be added later)
    Optional<User> findByFirebaseUid(String firebaseUid);
    Optional<User> findByEmail(String email);

    boolean existsByFirebaseUid(String firebaseUid);
    boolean existsByEmail(String email);
} 