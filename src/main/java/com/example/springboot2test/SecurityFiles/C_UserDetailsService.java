package com.example.springboot2test.SecurityFiles;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.springboot2test.Models.AdminData;
import com.example.springboot2test.Models.DoctorData;
import com.example.springboot2test.Models.UserData;
import com.example.springboot2test.Repository.AdminDataRepo;
import com.example.springboot2test.Repository.DoctorDataRepo;
import com.example.springboot2test.Repository.UserDataRepo;

@Service
public class C_UserDetailsService implements UserDetailsService{

    @Autowired
    UserDataRepo userDataRepo;

    @Autowired
    DoctorDataRepo doctorDataRepo;

    @Autowired
    AdminDataRepo adminDataRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

            UserData userData = userDataRepo.findByEmail(email);
            if (userData != null) {
            return new User(userData.getEmail(), userData.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + userData.getRole())));
            }

            DoctorData doc = doctorDataRepo.findDoctorForLogin(email);
            if (doc != null) {
            return new User(doc.getEmail(), doc.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + doc.getRole())));
            }

            AdminData admin = adminDataRepo.findByEmail(email);
            if (admin != null) {
            return new User(admin.getEmail(), admin.getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + admin.getRole())));
            }

            throw new UsernameNotFoundException("Details not found: " + email);
    }
    
}
