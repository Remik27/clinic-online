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
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.VisitsDtos;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.PatientService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.zajavka.util.DomainFixtures.*;
import static pl.zajavka.util.DtoFixtures.someVisitDto1;
import static pl.zajavka.util.DtoFixtures.someVisitDto2;

@WebMvcTest(controllers = VisitRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class VisitRestControllerWebMvcTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @MockBean
    private VisitService visitService;

    @MockBean
    private VisitMapper visitMapper;

    @MockBean
    private PatientService patientService;

    @Test
    public void cancelVisitCanCancelVisitCorrectly() throws Exception {
        //given
        Integer visitId = 1;
        Visit cancelledVisit = someVisit1().withId(visitId);
        VisitDto visitDto = someVisitDto1().withId(visitId);

        //when
        Mockito.when(visitService.cancelVisit(visitId)).thenReturn(cancelledVisit);
        Mockito.when(visitMapper.mapToDto(cancelledVisit)).thenReturn(visitDto);

        //then
        MvcResult result = mockMvc.perform(
                        put(VisitRestController.VISIT_API + VisitRestController.CANCEL_VISIT, visitId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(visitDto.getId()))).andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(visitDto));
    }

    @Test
    public void getVisitsCanReturnVisitsCorrectly() throws Exception {
        //given
        String pesel = "12345678901";
        Patient patient = somePatient().withPesel(pesel);
        List<Visit> visits = List.of(someVisit1().withPatient(patient), someVisit2().withPatient(patient));
        List<VisitDto> visitsDto = List.of(someVisitDto1().withPatientPesel(pesel),
                someVisitDto2().withPatientPesel(pesel));
        VisitsDtos visitsDtos = VisitsDtos.builder().visits(visitsDto).build();

//when
        Mockito.when(patientService.findPatientByPesel(pesel)).thenReturn(patient);
        Mockito.when(visitService.getListVisit(patient, Visit.Status.UPCOMING)).thenReturn(visits);
        Mockito.when(visitMapper.mapToDto(Mockito.eq(visits.get(0)))).thenReturn(visitsDto.get(0));
        Mockito.when(visitMapper.mapToDto(Mockito.eq(visits.get(1)))).thenReturn(visitsDto.get(1));

        //then
        MvcResult result = mockMvc.perform(get(VisitRestController.VISIT_API + VisitRestController.GET_VISITS_BY_PESEL, pesel))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(visitsDtos));
    }

    @Test
    public void deleteCancelledVisitsDeleteCorrectly() throws Exception {
        //given
        List<Visit> cancelledVisits = List.of(
                                someVisit1().withStatus(Visit.Status.CANCELLED),
                                someVisit2().withStatus(Visit.Status.CANCELLED));
//when
        Mockito.when(visitService.findCancelledVisits()).thenReturn(cancelledVisits);
        Mockito.doNothing().when(visitService).delete(cancelledVisits);

        //then
        mockMvc.perform(delete(VisitRestController.VISIT_API + VisitRestController.DELETE_CANCELLED_VISIT))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));
    }
}
