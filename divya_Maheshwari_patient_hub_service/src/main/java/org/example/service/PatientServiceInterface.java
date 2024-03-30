package org.example.service;

import org.example.model.Patient;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface PatientServiceInterface {
    @Cacheable(value = "patients", key = "#id")
    Patient getPatientById(Long id);

    @Cacheable(value = "allPatients", unless = "#result.size() == 0")
    List<Patient> getAllPatients();

    @CachePut(value = "patients", key = "#result.id")
    Patient createPatient(Patient patient);

    @CachePut(value = "patients", key = "#id")
    Patient updatePatient(Long id, Patient patientDetails);

    @CacheEvict(value = "patients", key = "#id")
    void deletePatient(Long id);
}
