package com.example.springboot2test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.springboot2test.Models.AdminData;
import com.example.springboot2test.Repository.AdminDataRepo;
import com.example.springboot2test.SecurityFiles.JwtsUtil;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@RestController
@CrossOrigin(origins = {"https://hospital-appointment-frontend-gamma.vercel.app", "http://localhost:3000"})
public class AdminAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtsUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminDataRepo adminDataRepo;

    @PostMapping("/adminlogin")
    public ResponseEntity<?> loginAdmin(@RequestBody AdminData adminData) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(adminData.getEmail(), adminData.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String actualRole = userDetails.getAuthorities().iterator().next().getAuthority();
            if (!actualRole.equals("ROLE_ADMIN")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                    .body(Map.of("error", "Access denied"));
            }
            String token = jwtUtil.generateToken(userDetails.getUsername());

            return ResponseEntity.ok(Map.of("token", token));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid admin credentials"));
        }
    }

    @PostConstruct
    public void initSuperAdmin() {
        String superAdminEmail = "dhineshkannag@gmail.com";
        
        if (adminDataRepo.findByEmail(superAdminEmail) == null) {
            AdminData adminData = new AdminData();
            adminData.setName("ShivaBala");
            adminData.setEmail(superAdminEmail);
            adminData.setPassword(passwordEncoder.encode("ShivaBala@123")); 
            adminData.setRole("ADMIN");

            adminDataRepo.save(adminData);
        }
    }
}