package org.example.controller;

import org.example.model.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface PatientControllerInterface {
    @GetMapping("/{id}")
    ResponseEntity<Patient> getPatient(@PathVariable Long id);

    @GetMapping("/")
    List<Patient> getAllPatients();

    @PostMapping("/")
    ResponseEntity<Patient> createPatient(@RequestBody Patient patient);

    @PutMapping("/{id}")
    ResponseEntity<Patient> updatePatient(@PathVariable Long id, @RequestBody Patient patientDetails);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePatient(@PathVariable Long id);
}
