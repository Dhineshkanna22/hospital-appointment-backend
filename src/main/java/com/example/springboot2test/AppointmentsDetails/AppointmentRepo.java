package com.example.springboot2test.AppointmentsDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    
    public interface AppointmentInfo {
        Long getId();
        String getPatientName();
        String getPatientEmail();
        String getAppointmentDate();
        String getAppointmentTime();
        String getDoctorName();
    }
    
    @Query("SELECT a.id as id, a.patientName as patientName, a.patientEmail as patientEmail, " +
           "a.appointmentDate as appointmentDate, a.appointmentTime as appointmentTime, " +
           "a.doctor.name as doctorName FROM Appointment a WHERE a.doctor.id = :doctorId")
    List<AppointmentInfo> findByDoctorId(@Param("doctorId") int doctorId);
    
    @Query("SELECT a.id as id, a.patientName as patientName, a.patientEmail as patientEmail, " +
           "a.appointmentDate as appointmentDate, a.appointmentTime as appointmentTime, " +
           "a.doctor.name as doctorName FROM Appointment a WHERE a.patientEmail = :patientEmail")
    List<AppointmentInfo> findByPatientEmail(@Param("patientEmail") String patientEmail);
    
    @SuppressWarnings("null")
    List<Appointment> findAll();
}