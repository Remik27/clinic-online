package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.api.dto.FreeTermsDtos;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.FreeTermMapper;
import pl.zajavka.api.dto.mapper.PatientMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.FreeTermService;
import pl.zajavka.business.PatientService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.security.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static pl.zajavka.integration.support.SecurityContextHolderSupport.setSecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(controllers = FreeTermsController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FreeTermsControllerWebMvcTest {
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private FreeTermService freeTermService;

    @MockBean
    private FreeTermMapper freeTermMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientMapper patientMapper;

    @MockBean
    private VisitMapper visitMapper;

    @MockBean
    private VisitService visitService;

    @MockBean
    private UserService userService;

    @Test
    public void selectSpecializationCanShowViewCorrectly() throws Exception {
        //given
        List<String> specializations = Arrays.asList("Specialization1", "Specialization2");

        //when, then
        Mockito.when(doctorService.getSpecializations()).thenReturn(specializations);

        mockMvc.perform(get("/select-specialization"))
                .andExpect(status().isOk())
                .andExpect(view().name("select-specialization"))
                .andExpect(model().attributeExists("specializations"));
    }

    @Test
    public void showFreeTermsCanShowViewCorrectly() throws Exception {
        //given
        List<FreeTermDto> freeTermDtos = new ArrayList<>();

        //when, then
        Mockito.when(freeTermService.getTermsBySpecialization(Mockito.anyString())).thenReturn(new ArrayList<>());
        Mockito.when(freeTermMapper.mapToDto(Mockito.any(FreeTerm.class))).thenReturn(new FreeTermDto());

        mockMvc.perform(get("/free-terms").param("specialization", "Specialization"))
                .andExpect(status().isOk())
                .andExpect(view().name("free-terms-list"))
                .andExpect(model().attributeExists("freeTermDtos"));
    }

    @Test
    public void prepareBookingTermCanShowViewCorrectly() throws Exception {

        //when, then
        Mockito.when(freeTermService.getTerm(Mockito.anyInt())).thenReturn(new FreeTerm());
        Mockito.when(freeTermMapper.mapToDto(Mockito.any(FreeTerm.class))).thenReturn(new FreeTermDto());

        mockMvc.perform(get("/book-term/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-term"))
                .andExpect(model().attributeExists("term", "patientDto"));
    }

    @Test
    public void bookTermCanShowViewForExistingPatientCorrectly() throws Exception {
        //given
        PatientDto patientDto = new PatientDto().withName("").withSurname("").withPesel("22222222222");
        Patient patient = new Patient().withName("some").withSurname("surname").withPesel("22222222222");
        VisitDto visitDto = new VisitDto();
        Visit visit = new Visit().withPatient(patient);
        FreeTerm freeTerm = new FreeTerm();

        //when, then
        Mockito.when(freeTermService.getTerm(Mockito.anyInt())).thenReturn(freeTerm);
        Mockito.when(patientService.findPatientByPesel(Mockito.any(String.class))).thenReturn(patient);
        Mockito.when(freeTermService.bookTerm(patient, freeTerm)).thenReturn(visit);
        Mockito.when(visitMapper.mapToDto(Mockito.any(Visit.class))).thenReturn(visitDto.withDoctorName("doctor name"));

        mockMvc.perform(post("/book-term")
                        .param("termId", "1")
                        .flashAttr("patientDto", patientDto))
                .andExpect(status().isOk())
                .andExpect(view().name("booked-successfully"))
                .andExpect(model().attributeExists("visit"));
    }
    @Test
    public void bookTermCanShowViewForNewPatientCorrectly() throws Exception {
        //given
        PatientDto patientDto = new PatientDto().withName("some").withSurname("surname").withPesel("22222222222");
        Patient patient = new Patient().withName("some").withSurname("surname").withPesel("22222222222");
        VisitDto visitDto = new VisitDto();
        Visit visit = new Visit().withPatient(patient);
        FreeTerm freeTerm = new FreeTerm();

        //when, then
        Mockito.when(freeTermService.getTerm(Mockito.anyInt())).thenReturn(freeTerm);
        Mockito.when((patientMapper.mapFromDto(patientDto))).thenReturn(patient);
        Mockito.when(patientService.addPatient(Mockito.any(Patient.class))).thenReturn(patient);
        Mockito.when(freeTermService.bookTerm(patient, freeTerm)).thenReturn(visit);
        Mockito.when(visitMapper.mapToDto(Mockito.any(Visit.class))).thenReturn(visitDto.withDoctorName("doctor name"));

        mockMvc.perform(post("/book-term")
                        .param("termId", "1")
                        .flashAttr("patientDto", patientDto))
                .andExpect(status().isOk())
                .andExpect(view().name("booked-successfully"))
                .andExpect(model().attributeExists("visit"));
    }


    @Test
    public void addNewTermsCanShowViewCorrectly() throws Exception {
        //given
        Doctor doctor = new Doctor().withClinicUserId(1);
        FreeTermsDtos freeTermDtos = new FreeTermsDtos(new ArrayList<>());
        setSecurityContextHolder();

        //when, then
        Mockito.when(doctorService.findDoctorByClinicUserId(Mockito.anyInt())).thenReturn(doctor);
        Mockito.when(freeTermMapper.mapFromDto(Mockito.any(FreeTermDto.class))).thenReturn(new FreeTerm());
        Mockito.when(freeTermService.addFreeTerms(Mockito.anyList())).thenReturn(new ArrayList<>());

        mockMvc.perform(post("/add-free-terms")
                        .flashAttr("freeTermDtos", freeTermDtos))
                .andExpect(status().isOk())
                .andExpect(view().name("added-successfully"));
    }

    @Test
    public void addFreeTermsFormCanShowViewCorrectly() throws Exception {
        // given
        List<FreeTermDto> freeTermDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            freeTermDtoList.add(FreeTermDto.builder().build());
        }
        FreeTermsDtos freeTermDtos = new FreeTermsDtos(freeTermDtoList);

        //when, then
        mockMvc.perform(get("/add-free-terms-form"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-free-terms-form"))
                .andExpect(model().attributeExists("freeTermDtos"));
    }
}
