package com.example.springboot2test.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.springboot2test.Models.DoctorData;

@Repository
public interface DoctorDataRepo extends JpaRepository<DoctorData, Integer> {

    DoctorData findByEmail(String email);

    @Query("SELECT new com.example.springboot2test.Models.DoctorData(d.id, d.name, d.email, d.password, null, d.specialization, null, d.role, null, null, null) " +
           "FROM DoctorData d WHERE d.email = :email")
    DoctorData findDoctorForLogin(@Param("email") String email);

    @Query("SELECT d.id FROM DoctorData d WHERE d.email = :email")
    Integer findIdByEmail(String email);
}

