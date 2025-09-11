package com.example.springboot2test.AppointmentsDetails;

import com.example.springboot2test.Models.DoctorData;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String patientName;

    private String patientEmail;

    private String appointmentDate;

    private String appointmentTime;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private DoctorData doctor;
}
