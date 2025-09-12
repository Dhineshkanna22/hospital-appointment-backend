package com.example.springboot2test.AppointmentsDetails;

import com.example.springboot2test.Models.DoctorData;
import com.example.springboot2test.Repository.DoctorDataRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = {"https://hospital-appointment-frontend-gamma.vercel.app", "http://localhost:3000"})
public class AppointmentController {

    @Autowired
    private AppointmentRepo appointmentRepo;

    @Autowired
    private DoctorDataRepo doctorDataRepo;
    
    @PostMapping("/user/appointments")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        DoctorData doctor = doctorDataRepo.findById(request.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        Appointment appointment = new Appointment();
        appointment.setPatientName(request.getPatientName());
        appointment.setPatientEmail(request.getPatientEmail());
        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setDoctor(doctor);

        appointmentRepo.save(appointment);

        return ResponseEntity.ok("Appointment booked successfully ✅");
    }

    @GetMapping("/doctor/appointments/{email}")
    public ResponseEntity<?> getAppointmentsByDoctor(@PathVariable String email) {
        Integer doctorId = doctorDataRepo.findIdByEmail(email);
        
        if (doctorId == null) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        
        List<AppointmentRepo.AppointmentInfo> appointments = appointmentRepo.findByDoctorId(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/user/appointments/patient/{email}")
    public ResponseEntity<?> getAppointmentsByPatient(@PathVariable String email) {
        List<AppointmentRepo.AppointmentInfo> appointments = appointmentRepo.findByPatientEmail(email);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/admin/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return ResponseEntity.ok(appointmentRepo.findAll());
    }
}
