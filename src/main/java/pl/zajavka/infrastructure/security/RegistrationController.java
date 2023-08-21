package pl.zajavka.infrastructure.security;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.domain.Doctor;

import java.util.List;

@Controller
@AllArgsConstructor
public class RegistrationController {

    public static final String REGISTRATION = "/registration";


    private final UserService userService;

    @GetMapping(REGISTRATION)
    public ModelAndView registration(Model model) {
        UserDto userDto = UserDto.builder().role(Roles.DOCTOR).build();
        List<String> availableRoles = List.of(Roles.DOCTOR.name(), Roles.PATIENT.name());
        List<Doctor.Specialization> availableSpecializations = List.of(Doctor.Specialization.values());

        model.addAttribute("userDto", userDto);
        model.addAttribute("availableRoles", availableRoles);
        model.addAttribute("availableSpecializations", availableSpecializations);

        return new ModelAndView("registration");
    }

    @PostMapping(REGISTRATION)
    public ModelAndView registrationFrom(@Valid @ModelAttribute("userDto") UserDto userDto,
                                         BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<String> availableRoles = List.of(Roles.DOCTOR.name(), Roles.PATIENT.name());
            List<Doctor.Specialization> availableSpecializations = List.of(Doctor.Specialization.values());
            model.addAttribute("userDto", userDto);
            model.addAttribute("availableRoles", availableRoles);
            model.addAttribute("availableSpecializations", availableSpecializations);
            return new ModelAndView("registration");
        }
        userService.saveUser(userDto);
        return new ModelAndView("registered-successfully");
    }
}
