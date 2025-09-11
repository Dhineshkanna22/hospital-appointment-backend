package com.example.springboot2test.Models;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private String specialization;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT")
    private String about;

    private String role;

    @Lob
    @Basic(fetch = FetchType.LAZY)  
    @Column(columnDefinition = "TEXT")
    private String imageName;
     
    private String imageType;

    @Lob
    @Basic(fetch = FetchType.LAZY) 
    private byte[] imageData; 
}

