package com.example.springboot2test.AppointmentsDetails;

import lombok.Data;

@Data
public class AppointmentRequest {
    private String patientName;
    private String patientEmail;
    private String appointmentDate;
    private String appointmentTime;
    private int doctorId; 
}
