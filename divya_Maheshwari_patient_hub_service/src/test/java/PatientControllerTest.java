import org.example.controller.PatientController;
import org.example.controller.PatientControllerInterface;
import org.example.model.Patient;
import org.example.service.PatientServiceInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientServiceInterface patientService;

    @InjectMocks
    private PatientController patientController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }


    @Test
    public void testCreatePatient() throws Exception {
        Patient patient = new Patient(1L, "John Doe", 30, "Flu");
        given(patientService.createPatient(any(Patient.class))).willReturn(patient);

        mockMvc.perform(post("/api/patients/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\", \"age\": 30, \"diagnosis\": \"Flu\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    public void testGetPatient() throws Exception {
        Patient patient = new Patient(1L, "John Doe", 30, "Flu");
        given(patientService.getPatientById(1L)).willReturn(patient);

        mockMvc.perform(get("/api/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }


    @Test
    public void testUpdatePatient() throws Exception {
        Patient updatedPatient = new Patient(1L, "Jane Doe", 31, "Cold");
        given(patientService.updatePatient(eq(1L), any(Patient.class))).willReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Jane Doe\", \"age\": 31, \"diagnosis\": \"Cold\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.age").value(31));
    }
    @Test
    public void testDeletePatient() throws Exception {
        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllPatients() throws Exception {
        List<Patient> patients = Arrays.asList(
                new Patient(1L, "John Doe", 30, "Flu"),
                new Patient(2L, "Jane Doe", 31, "Cold")
        );
        given(patientService.getAllPatients()).willReturn(patients);

        mockMvc.perform(get("/api/patients/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

}
