package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.DoctorDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.DoctorMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.security.UserService;

import java.util.List;

@Controller
@AllArgsConstructor
public class DoctorController {

    public static final String DOCTOR_PANEL = "/doctor-panel";
    public static final String UPCOMING_VISITS = "/doctor/upcoming-visits";
    public static final String VISIT_DETAILS = "/doctor/visit-details/{visitId}";
    public static final String FINISHED_VISITS = "/doctor/finished-visits";
    public static final String ADD_DESCRIPTION = "/add-description";
    public static final String ADD_DIAGNOSIS = "/add-diagnosis";
    public static final String FINISH_VISIT = "/finish-visit/{visitId}";
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;
    private final VisitMapper visitMapper;
    private final VisitService visitService;
    private final UserService userService;


    @GetMapping(DOCTOR_PANEL)
    public ModelAndView showDoctorPanel(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Doctor doctor = doctorService.findDoctorByClinicUserId(userService.getUserId(username));

        DoctorDto doctorDto = doctorMapper.mapToDto(doctor);
        model.addAttribute("doctorDto",doctorDto);


        return new ModelAndView("doctor-panel");
    }
    @GetMapping(UPCOMING_VISITS)
    public ModelAndView showUpcomingVisits(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Doctor doctor = doctorService.findDoctorByClinicUserId(userService.getUserId(username));
        List<VisitDto> visits = doctorService.getVisitsByDoctorId(doctor.getId(), Visit.Status.UPCOMING).stream()
                .map(visitMapper::mapToDto).toList();
        model.addAttribute("visitDtos",visits);


        return new ModelAndView("doctor-upcoming-visits");
    }
    @GetMapping(FINISHED_VISITS)
    public ModelAndView showFinishedVisits(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Doctor doctor = doctorService.findDoctorByClinicUserId(userService.getUserId(username));
        List<VisitDto> visits = doctorService.getVisitsByDoctorId(doctor.getId(), Visit.Status.DONE).stream()
                .map(visitMapper::mapToDto).toList();
        model.addAttribute("visitDtos",visits);


        return new ModelAndView("doctor-finished-visits");
    }
    @GetMapping(VISIT_DETAILS)
    public ModelAndView showVisitDetails(@PathVariable String visitId, Model model){
        Visit visit = visitService.findVisitById(Integer.valueOf(visitId));
        VisitDto visitDto = visitMapper.mapToDto(visit);
        model.addAttribute("visitDto", visitDto);
        return new ModelAndView("doctor-visit-details");
    }

    @PutMapping(ADD_DESCRIPTION)
    public ModelAndView addDescription(@ModelAttribute("VisitDto") VisitDto visitDto, Model model){
        Visit visit = visitService.findVisitById(visitDto.getId());
        String description = visitDto.getDescription();
        visitService.addDescription(visit, description);
        return new ModelAndView("redirect:/doctor/finished-visits");
    }
    @PutMapping(ADD_DIAGNOSIS)
    public ModelAndView addDiagnosis(@ModelAttribute("VisitDto") VisitDto visitDto, Model model){
        Visit visit = visitService.findVisitById(visitDto.getId());
        String disease = visitDto.getDisease();
        visitService.addDisease(visit, disease);
        return new ModelAndView("redirect:/doctor/finished-visits");
    }
    @PutMapping(FINISH_VISIT)
    public ModelAndView finishVisit(@PathVariable String visitId, Model model){
        visitService.finishVisit(visitId);
        return new ModelAndView("redirect:/doctor/upcoming-visits");
    }

}
