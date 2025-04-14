package com.saasautomationbuilder.backend.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
// import com.google.firebase.auth.FirebaseToken; // Not directly used here
import com.google.firebase.auth.UserRecord;
import com.saasautomationbuilder.backend.domain.User;
import com.saasautomationbuilder.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    /**
     * Finds a user by Firebase UID, creating or updating them in the local DB if necessary.
     * This should be called after the Firebase token is validated.
     *
     * @param firebaseUid The validated Firebase UID.
     * @return The corresponding User entity from the local database.
     * @throws RuntimeException if user details cannot be fetched from Firebase or DB save fails.
     */
    @Transactional
    public User findOrCreateUser(String firebaseUid) {
        Optional<User> existingUser = userRepository.findByFirebaseUid(firebaseUid);

        if (existingUser.isPresent()) {
            // Optional: Update local user details if needed (e.g., display name)
            // Consider adding logic here if you want to sync updates from Firebase
            return existingUser.get();
        } else {
            logger.info("User with Firebase UID {} not found locally, creating new user...", firebaseUid);
            try {
                // Fetch user details from Firebase Auth to populate local record
                UserRecord firebaseUser = FirebaseAuth.getInstance().getUser(firebaseUid);

                User newUser = new User();
                newUser.setFirebaseUid(firebaseUid);
                newUser.setEmail(firebaseUser.getEmail()); // Ensure email is available
                newUser.setDisplayName(firebaseUser.getDisplayName()); // Optional
                // Timestamps createdAt/updatedAt are set automatically by annotations

                return userRepository.save(newUser);
            } catch (FirebaseAuthException e) {
                logger.error("Failed to fetch user details from Firebase for UID: {}", firebaseUid, e);
                throw new RuntimeException("Failed to retrieve Firebase user details", e);
            } catch (Exception e) {
                logger.error("Failed to save new user with Firebase UID: {}", firebaseUid, e);
                throw new RuntimeException("Failed to save new user", e);
            }
        }
    }

    // Optional: Method to get user by UID without creation
    @Transactional(readOnly = true)
    public Optional<User> findByFirebaseUid(String firebaseUid) {
        return userRepository.findByFirebaseUid(firebaseUid);
    }
} 