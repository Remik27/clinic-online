package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajavka.api.dto.DiseaseHistoryDto;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.PatientMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.PatientService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.DiseaseHistory;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.security.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.zajavka.integration.support.SecurityContextHolderSupport.setSecurityContextHolder;
import static pl.zajavka.util.DomainFixtures.*;
import static pl.zajavka.util.DtoFixtures.*;

@WebMvcTest(controllers = PatientController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PatientControllerWebMvcTest {
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientMapper patientMapper;

    @MockBean
    private VisitService visitService;

    @MockBean
    private VisitMapper visitMapper;

    @MockBean
    private UserService userService;


    @Test
    public void showPatientPanelCanShowViewCorrectly() throws Exception {
//given
        setSecurityContextHolder();
        Patient patient = somePatient().withClinicUserId(1);
        PatientDto patientDto = somePatientDto();

        //when
        Mockito.when(patientService.findPatientByClinicUserId(Mockito.any())).thenReturn(patient);
        Mockito.when(patientMapper.mapToDto(patient)).thenReturn(patientDto);

        //then
        mockMvc.perform(get("/patient-panel"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-panel"))
                .andExpect(model().attributeExists("patientDto"));
    }

    @Test
    public void showFinishedVisitsCanShowViewCorrectly() throws Exception {
        //given
        Patient patient = somePatient().withPesel("12311111111");
        List<Visit> visits = List.of(someVisit1(), someVisit2());
        VisitDto visitDto1 = someVisitDto1();
        VisitDto visitDto2 = someVisitDto2();

        //when
        Mockito.when(patientService.findPatientByPesel("12311111111")).thenReturn(patient);
        Mockito.when(visitService.getListVisit(patient, Visit.Status.DONE)).thenReturn(visits);
        Mockito.when(visitMapper.mapToDto(someVisit1())).thenReturn(visitDto1);
        Mockito.when(visitMapper.mapToDto(someVisit2())).thenReturn(visitDto2);

        //then
        mockMvc.perform(get("/finished-visits/12311111111"))
                .andExpect(status().isOk())
                .andExpect(view().name("finished-visits"))
                .andExpect(model().attributeExists("visitDtos"));
    }

    @Test
    public void showUpcomingVisitsCanShowViewCorrectly() throws Exception {
        //given
        Patient patient = somePatient().withPesel("12311111111");
        List<Visit> visits = List.of(someVisit1().withStatus(Visit.Status.UPCOMING),
                someVisit2().withStatus(Visit.Status.UPCOMING));
        VisitDto visitDto1 = someVisitDto1().withStatus(Visit.Status.UPCOMING);
        VisitDto visitDto2 = someVisitDto2().withStatus(Visit.Status.UPCOMING);

        //when
        Mockito.when(patientService.findPatientByPesel("12311111111")).thenReturn(patient);
        Mockito.when(visitService.getListVisit(patient, Visit.Status.UPCOMING)).thenReturn(visits);
        Mockito.when(visitMapper.mapToDto(someVisit1().withStatus(Visit.Status.UPCOMING))).thenReturn(visitDto1);
        Mockito.when(visitMapper.mapToDto(someVisit2().withStatus(Visit.Status.UPCOMING))).thenReturn(visitDto2);

        //then
        mockMvc.perform(get("/upcoming-visits/12311111111"))
                .andExpect(status().isOk())
                .andExpect(view().name("upcoming-visits"))
                .andExpect(model().attributeExists("visitDtos"));
    }

    @Test
    public void showVisitDetailsCanShowViewCorrectly() throws Exception {
        //given
        Visit visit = someVisit1();
        VisitDto visitDto = someVisitDto1();

        //when
        Mockito.when(visitService.findVisitById(1)).thenReturn(visit);
        Mockito.when(visitMapper.mapToDto(visit)).thenReturn(visitDto);

        //then
        mockMvc.perform(get("/visit-details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("visit-details"))
                .andExpect(model().attributeExists("visitDto"));
    }

    @Test
    public void cancelVisitCanShowViewCorrectly() throws Exception {
        //given
        Visit visit = someVisit1().withId(1);
        VisitDto visitDto = someVisitDto1().withId(1);

        //when
        Mockito.when(visitService.cancelVisit(1)).thenReturn(visit);
        Mockito.when(visitMapper.mapToDto(visit)).thenReturn(visitDto);

        //then
        mockMvc.perform(put("/cancel-visit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cancelled-successfully"))
                .andExpect(model().attributeExists("visitDto"));
    }

    @Test
    public void showPatientHistoryCanShowViewCorrectly() throws Exception {
        //given
        Patient patient = somePatient();
        DiseaseHistory diseaseHistory = someDiseaseHistory();
        DiseaseHistoryDto diseaseHistoryDto = someDiseaseHistoryDto();

        //when
        Mockito.when(patientService.findPatientByVisitId(1)).thenReturn(patient);
        Mockito.when(patientService.showDiseaseHistory(patient)).thenReturn(diseaseHistory);
        Mockito.when(patientMapper.mapDiseaseHistory(diseaseHistory)).thenReturn(diseaseHistoryDto);

        //then
        mockMvc.perform(get("/patient-history/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("disease-history"))
                .andExpect(model().attributeExists("diseaseHistoryDto"));
    }




}
