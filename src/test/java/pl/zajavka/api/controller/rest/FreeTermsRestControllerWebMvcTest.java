package pl.zajavka.api.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
import pl.zajavka.util.DtoFixtures;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.zajavka.util.DomainFixtures.*;
import static pl.zajavka.util.DtoFixtures.somePatientDto;
import static pl.zajavka.util.DtoFixtures.someVisitDto1;

@WebMvcTest(controllers = FreeTermsRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FreeTermsRestControllerWebMvcTest {
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private FreeTermService freeTermService;

    @MockBean
    private FreeTermMapper freeTermMapper;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientMapper patientMapper;

    @MockBean
    private VisitService visitService;

    @MockBean
    private VisitMapper visitMapper;

    @Test
    public void getFreeTermsCanReturnTermsCorrectly() throws Exception {
        //given
        String specialization = Doctor.Specialization.OKULISTA.name();
        List<FreeTerm> terms = List.of(someTerm1(), someTerm2());

        //when
        Mockito.when(freeTermService.getTermsBySpecialization(specialization)).thenReturn(terms);
        Mockito.when(freeTermMapper.mapToDto(someTerm1())).thenReturn(DtoFixtures.someTermDto1());
        Mockito.when(freeTermMapper.mapToDto(someTerm2())).thenReturn(DtoFixtures.someTermDto2());

        //then
        mockMvc.perform(
                        get(FreeTermsRestController.FREE_TERMS_API + FreeTermsRestController.GET_FREE_TERMS,
                                specialization)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void fetSpecializationsCanReturnSpecializationsCorrectly() throws Exception {
        //given
        List<String> specializations = Arrays.stream(Doctor.Specialization.values()).map(Enum::name).toList();

        //when
        Mockito.when(doctorService.getSpecializations()).thenReturn(specializations);

        //then
        mockMvc.perform(get(FreeTermsRestController.FREE_TERMS_API + FreeTermsRestController.GET_SPECIALIZATIONS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.specializations", is(specializations)));
    }

    @Test
    public void bookTermCanBookTermCorrectly() throws Exception {
        //given
        Integer termId = 1;
        PatientDto patientDto = somePatientDto();
        Patient patient = somePatient();
        FreeTerm freeTerm = someTerm1().withId(termId);
        Visit visit = someVisit1();
        VisitDto visitDto = someVisitDto1();

        //when
        Mockito.when(patientMapper.mapFromDto(patientDto)).thenReturn(patient);
        Mockito.when(patientService.checkExistencePatient(patient)).thenReturn(true);
        Mockito.when(patientService.findPatientByPesel(patientDto.getPesel())).thenReturn(patient);
        Mockito.when(freeTermService.getTerm(termId)).thenReturn(freeTerm);
        Mockito.when(visitService.buildVisit(patient, freeTerm)).thenReturn(visit);
        Mockito.when(visitMapper.mapToDto(visit)).thenReturn(visitDto);
        //then

        MvcResult result = mockMvc.perform(
                post(FreeTermsRestController.FREE_TERMS_API + FreeTermsRestController.BOOK_TERM,
                        termId)
                        .content(objectMapper.writeValueAsString(patientDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientPesel", is(patientDto.getPesel())))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(visitDto));
    }


}
