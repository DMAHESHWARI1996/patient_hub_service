package org.example.service;

import org.example.exception.ResourceNotFoundException;
import org.example.model.Patient;
import org.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService implements PatientServiceInterface {

    @Autowired
    private PatientRepository patientRepository;

    /**
     * Retrieves a patient by their ID.
     * @param id The ID of the patient.
     * @return The found patient.
     * @throws ResourceNotFoundException if no patient is found.
     */
    @Override
    @Cacheable(value = "patients", key = "#id")
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    /**
     * Retrieves all patients.
     * @return A list of patients.
     */
    @Override
    @Cacheable(value = "allPatients", unless = "#result.size() == 0")
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    /**
     * Creates a new patient.
     * @param patient The patient to create.
     * @return The created patient.
     */
    @Override
    @CachePut(value = "patients", key = "#result.id")
    public Patient createPatient(Patient patient) {
        validatePatient(patient);
        return patientRepository.save(patient);
    }

    /**
     * Updates an existing patient.
     * @param id The ID of the patient to update.
     * @param patientDetails Details to update.
     * @return The updated patient.
     * @throws ResourceNotFoundException if no patient is found.
     */
    @Override
    @CachePut(value = "patients", key = "#id")
    public Patient updatePatient(Long id, Patient patientDetails) {
        Patient patient = getPatientById(id); // This will throw ResourceNotFoundException if patient does not exist
        validatePatient(patientDetails);

        patient.setName(patientDetails.getName());
        patient.setAge(patientDetails.getAge());
        patient.setDiagnosis(patientDetails.getDiagnosis());

        return patientRepository.save(patient);
    }

    /**
     * Deletes a patient by their ID.
     * @param id The ID of the patient to delete.
     */
    @Override
    @CacheEvict(value = "patients", key = "#id")
    public void deletePatient(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new ResourceNotFoundException("Patient not found");
        }
        patientRepository.deleteById(id);
    }

    /**
     * Validates the patient details.
     * @param patient The patient to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    private void validatePatient(Patient patient) {
        if (patient.getName() == null || patient.getName().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        if (patient.getAge() <= 0) {
            throw new IllegalArgumentException("Patient age must be positive");
        }
        // Add more validations as necessary
    }
}
