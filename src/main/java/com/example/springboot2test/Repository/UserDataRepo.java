package com.example.springboot2test.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.springboot2test.Models.UserData;


@Repository
public interface UserDataRepo extends JpaRepository<UserData, Long>{
    UserData findByEmail(String email);
}
