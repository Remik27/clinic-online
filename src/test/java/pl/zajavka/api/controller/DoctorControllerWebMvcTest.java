package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajavka.api.dto.DoctorDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.DoctorMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.security.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.zajavka.integration.support.SecurityContextHolderSupport.setSecurityContextHolder;

@WebMvcTest(controllers = DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private DoctorMapper doctorMapper;

    @MockBean
    private VisitMapper visitMapper;

    @MockBean
    private VisitService visitService;

    @MockBean
    private UserService userService;

    @Test
    public void testShowDoctorPanel() throws Exception {
        //given
        setSecurityContextHolder();

        Doctor doctor = new Doctor();
        DoctorDto doctorDto = new DoctorDto();

        //when, then
        Mockito.when(doctorService.findDoctorByClinicUserId(Mockito.any())).thenReturn(doctor);
        Mockito.when(doctorMapper.mapToDto(doctor)).thenReturn(doctorDto);


        mockMvc.perform(get("/doctor-panel"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor-panel"))
                .andExpect(model().attributeExists("doctorDto"));
    }

    @Test
    public void testShowUpcomingVisits() throws Exception {
        // given
        setSecurityContextHolder();

        Doctor doctor = new Doctor();
        DoctorDto doctorDto = new DoctorDto();

        // when, then
        Mockito.when(doctorService.findDoctorByClinicUserId(Mockito.any())).thenReturn(doctor);
        Mockito.when(doctorMapper.mapToDto(doctor)).thenReturn(doctorDto);

        mockMvc.perform(get("/doctor/upcoming-visits"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor-upcoming-visits"))
                .andExpect(model().attributeExists("visitDtos"));
    }

    @Test
    public void testShowFinishedVisits() throws Exception {

        // given
        setSecurityContextHolder();

        Doctor doctor = new Doctor();
        DoctorDto doctorDto = new DoctorDto();

        // when, then
        Mockito.when(doctorService.findDoctorByClinicUserId(Mockito.any())).thenReturn(doctor);
        Mockito.when(doctorMapper.mapToDto(doctor)).thenReturn(doctorDto);

        mockMvc.perform(get("/doctor/finished-visits"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor-finished-visits"))
                .andExpect(model().attributeExists("visitDtos"));
    }

    @Test
    public void testShowVisitDetails() throws Exception {
        // given
        Visit visit = new Visit();
        VisitDto visitDto = new VisitDto();

       //when, then
        Mockito.when(visitService.findVisitById(Mockito.anyInt())).thenReturn(visit);
        Mockito.when(visitMapper.mapToDto(visit)).thenReturn(visitDto);

        mockMvc.perform(get("/doctor/visit-details/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor-visit-details"))
                .andExpect(model().attributeExists("visitDto"));
    }

    @Test
    public void testAddDescription() throws Exception {
        // given
        Visit visit = new Visit();
        VisitDto visitDto = new VisitDto();

        //when, then
        Mockito.when(visitService.findVisitById(Mockito.anyInt())).thenReturn(visit);

        mockMvc.perform(put("/add-description")
                        .flashAttr("VisitDto", visitDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctor/finished-visits"));
    }

    @Test
    public void testAddDiagnosis() throws Exception {
        // given
        Visit visit = new Visit();
        VisitDto visitDto = new VisitDto();

        //when, then
        Mockito.when(visitService.findVisitById(Mockito.anyInt())).thenReturn(visit);

        mockMvc.perform(put("/add-diagnosis")
                        .flashAttr("VisitDto", visitDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctor/finished-visits"));
    }

    @Test
    public void testFinishVisit() throws Exception {


        mockMvc.perform(put("/finish-visit/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/doctor/upcoming-visits"));
    }




}

