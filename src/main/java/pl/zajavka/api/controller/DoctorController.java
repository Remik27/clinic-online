package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.DoctorDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.DoctorMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;

import java.util.Comparator;
import java.util.List;

@Controller
@AllArgsConstructor
public class DoctorController {

    public static final String DOCTOR_PANEL = "/doctor-panel";
    public static final String UPCOMING_VISITS = "/doctor/upcoming-visits";
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final VisitMapper visitMapper;


    @GetMapping(DOCTOR_PANEL)
    public ModelAndView showDoctorPanel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Doctor doctor = doctorService.findDoctorByClinicUsername(username);

        DoctorDto doctorDto = doctorMapper.mapToDto(doctor);
        model.addAttribute("doctorDto",doctorDto);


        return new ModelAndView("doctor-panel");
    }
    @GetMapping(UPCOMING_VISITS)
    public ModelAndView showUpcomingVisits(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Doctor doctor = doctorService.findDoctorByClinicUsername(username);
        List<VisitDto> visits = doctorService.getVisitsByDoctorId(doctor.getId(), Visit.Status.UPCOMING).stream()
                .map(visitMapper::mapToDto).toList();
        model.addAttribute("visitDtos",visits);


        return new ModelAndView("doctor-upcoming-visits");
    }
}
