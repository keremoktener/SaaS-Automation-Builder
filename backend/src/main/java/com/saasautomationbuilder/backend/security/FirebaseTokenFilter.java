package com.saasautomationbuilder.backend.security;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.saasautomationbuilder.backend.domain.User; // Import User entity
import com.saasautomationbuilder.backend.service.UserService; // Import UserService
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class FirebaseTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseTokenFilter.class);
    private final SecurityUtils securityUtils;
    private final UserService userService; // Inject UserService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String idToken = securityUtils.getTokenFromRequest(request);

        if (idToken != null) {
            FirebaseToken decodedToken = null;
            try {
                decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
                String uid = decodedToken.getUid();

                // Find or create the user in the local database
                User user = userService.findOrCreateUser(uid);
                logger.debug("Firebase token verified successfully for UID: {}, mapped to local User ID: {}", uid, user.getId());

                // Principal can be UID or the local User object depending on needs
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>()); // Use local User as principal

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (FirebaseAuthException e) {
                logger.warn("Invalid Firebase ID Token: {}", e.getMessage());
                SecurityContextHolder.clearContext();
            } catch (Exception e) {
                 // Catch exceptions during findOrCreateUser as well
                 logger.error("Error processing Firebase token or finding/creating user: {}", e.getMessage(), e);
                 SecurityContextHolder.clearContext();
            }
        } else {
             logger.trace("No Firebase ID Token found in request");
        }

        filterChain.doFilter(request, response);
    }
} 