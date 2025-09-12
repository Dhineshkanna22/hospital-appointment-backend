package com.example.springboot2test;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springboot2test.Models.DoctorData;
import com.example.springboot2test.Models.UserData;
import com.example.springboot2test.Repository.DoctorDataRepo;
import com.example.springboot2test.Repository.UserDataRepo;
import com.example.springboot2test.SecurityFiles.JwtsUtil;

import java.util.*;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@RestController
@CrossOrigin(origins = {"https://hospital-appointment-frontend-gamma.vercel.app", "http://localhost:3000"})
public class Controller {

    @Autowired
    private UserDataRepo userDataRepo;

    @Autowired
    private DoctorDataRepo doctorDataRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtsUtil jwtsUtil;

    // ------------------- USER REGISTER -------------------
    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody UserData userData) {
        if (userDataRepo.findByEmail(userData.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists!");
        }
        userData.setRole("USER");
        userData.setPassword(passwordEncoder.encode(userData.getPassword()));
        return ResponseEntity.ok(userDataRepo.save(userData));
    }

    // ------------------- DOCTOR REGISTER -------------------
    @PostMapping("/admin/doctorregister")
    public ResponseEntity<?> doctorRegister(
        @RequestPart DoctorData doctorData,
        @RequestPart MultipartFile imageFile) throws IOException {

        if (doctorDataRepo.findByEmail(doctorData.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Doctor already exists!");
        }

        doctorData.setPassword(passwordEncoder.encode(doctorData.getPassword()));
        doctorData.setRole("DOCTOR");

        doctorData.setImageName(imageFile.getOriginalFilename());
        doctorData.setImageType(imageFile.getContentType());
        doctorData.setImageData(imageFile.getBytes());

        return ResponseEntity.ok(doctorDataRepo.save(doctorData));
    }

    // ------------------- USER LOGIN -------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody UserData userData) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(userData.getEmail(), userData.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String actualRole = userDetails.getAuthorities().iterator().next().getAuthority();
        if (!actualRole.equals("ROLE_USER")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "❌ Access denied"));
        }
        String token = jwtsUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }

    // ------------------- DOCTOR LOGIN -------------------
    @PostMapping("/doctorlogin")
    public ResponseEntity<Map<String, String>> doctorLogin(@RequestBody DoctorData doctorData) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(doctorData.getEmail(), doctorData.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String actualRole = userDetails.getAuthorities().iterator().next().getAuthority();
        if (!actualRole.equals("ROLE_DOCTOR")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .body(Map.of("error", "❌ Access denied"));
        }
        String token = jwtsUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(Map.of("token", token));
    }

    // ------------------- ADMIN GET ALL -------------------
    @GetMapping("/admin/user")
    public ResponseEntity<List<UserData>> getAllUser() {
        return ResponseEntity.ok(userDataRepo.findAll());
    }

    @GetMapping("/user/doctor")
    public ResponseEntity<List<Map<String, Object>>> getAllDoctor() {
        List<DoctorData> doctors = doctorDataRepo.findAll();

        List<Map<String, Object>> doctorList = new ArrayList<>();
        for (DoctorData d : doctors) {
            Map<String, Object> docMap = new HashMap<>();
            docMap.put("id", d.getId());
            docMap.put("name", d.getName());
            docMap.put("specialization", d.getSpecialization());
            docMap.put("about", d.getAbout());
            docMap.put("imageName", d.getImageName());
            docMap.put("imageType", d.getImageType());
            doctorList.add(docMap);
        }

        return ResponseEntity.ok(doctorList);
    }

    @GetMapping("/user/image/{id}")
    public ResponseEntity<byte[]> getDoctorImageForUser(@PathVariable int id) {
        DoctorData doctor = doctorDataRepo.findById(id)
            .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(doctor.getImageType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doctor.getImageName() + "\"")
                .body(doctor.getImageData());
    }

}
