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
import pl.zajavka.api.dto.mapper.DoctorMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.domain.Doctor;

@Controller
@AllArgsConstructor
public class DoctorController {

    public static final String DOCTOR_PANEL = "/doctor-panel";
    private final DoctorService doctorService;
    private final DoctorMapper doctorMapper;

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
}
