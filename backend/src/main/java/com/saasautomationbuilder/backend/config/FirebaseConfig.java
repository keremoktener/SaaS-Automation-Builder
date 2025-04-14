package com.saasautomationbuilder.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);
    // Make sure the file name matches the one you downloaded and renamed
    private static final String SERVICE_ACCOUNT_KEY_PATH = "firebase-service-account-key.json";

    @PostConstruct
    public void initializeFirebaseApp() {
        // Check if FirebaseApp has already been initialized to prevent errors on reload
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                logger.info("Initializing Firebase Admin SDK...");

                Resource resource = new ClassPathResource(SERVICE_ACCOUNT_KEY_PATH);
                if (!resource.exists()) {
                    logger.error("Firebase service account key file not found at classpath:{}", SERVICE_ACCOUNT_KEY_PATH);
                    logger.error("Please ensure the file '{}' is present in the src/main/resources directory.", SERVICE_ACCOUNT_KEY_PATH);
                    throw new RuntimeException("Firebase service account key file not found: " + SERVICE_ACCOUNT_KEY_PATH);
                }

                InputStream serviceAccount = resource.getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    // Optional: Specify database URL if using Firebase Realtime Database
                    // .setDatabaseUrl("https://YOUR_PROJECT_ID.firebaseio.com")
                    .build();

                FirebaseApp.initializeApp(options);
                logger.info("Firebase Admin SDK initialized successfully.");

            } catch (IOException e) {
                logger.error("Error initializing Firebase Admin SDK", e);
                // Propagate exception to prevent application startup if Firebase fails
                throw new RuntimeException("Failed to initialize Firebase Admin SDK", e);
            } catch (Exception e) {
                logger.error("Unexpected error during Firebase Admin SDK initialization", e);
                throw new RuntimeException("Unexpected error during Firebase initialization", e);
            }
        } else {
            logger.warn("Firebase Admin SDK already initialized. Skipping re-initialization.");
        }
    }
}
