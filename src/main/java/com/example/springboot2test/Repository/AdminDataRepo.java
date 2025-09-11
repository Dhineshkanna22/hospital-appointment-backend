package com.example.springboot2test.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springboot2test.Models.AdminData;

@Repository
public interface AdminDataRepo extends JpaRepository<AdminData, Long> {
    AdminData findByEmail(String email);
}

