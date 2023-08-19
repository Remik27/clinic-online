package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.api.dto.FreeTermDtos;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.FreeTermMapper;
import pl.zajavka.api.dto.mapper.PatientMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.FreeTermService;
import pl.zajavka.business.PatientService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.security.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping()
@AllArgsConstructor
public class FreeTermsController {

    private static final String FREE_TERMS = "/free-terms";
    private static final String SELECT_SPEC = "/select-specialization";
    private static final String PREPARE_BOOKING = "/book-term/{freeTermId}";
    private static final String BOOK_TERM = "/book-term";
    private static final String ADD_FREE_TERMS = "/add-free-terms";
    private static final String ADD_FREE_TERMS_FORM = "/add-free-terms-form";
    private final DoctorService doctorService;
    private final FreeTermService freeTermService;
    private final FreeTermMapper freeTermMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final VisitMapper visitMapper;
    private final UserService userService;

    @GetMapping(value = SELECT_SPEC)
    public ModelAndView selectSpecialization(Model model) {
        List<String> specializations = doctorService.getSpecializations();
        model.addAttribute("specializations", specializations);

        return new ModelAndView("select-specialization");
    }

    @GetMapping(FREE_TERMS)
    public ModelAndView showFreeTerms(@RequestParam String specialization, Model model) {
        List<FreeTermDto> freeTerms = getTermsBySpecialization(specialization);
        model.addAttribute("freeTermDtos", freeTerms);
        return new ModelAndView("free-terms-list");
    }

    @GetMapping(PREPARE_BOOKING)
    public ModelAndView prepareBookingTerm(@PathVariable Integer freeTermId, Model model) {
        model.addAttribute("term", freeTermMapper.mapToDto(freeTermService.getTerm(freeTermId)));
        model.addAttribute("patientDto", PatientDto.builder().build());
        return new ModelAndView("book-term");
    }

    @PostMapping(BOOK_TERM)
    public ModelAndView bookTerm(
            @Valid @ModelAttribute("patientDto") PatientDto patientDto,
            @RequestParam("termId") Integer termId,
            Model model
    ) {
        Patient patient = preparePatient(patientDto);

        FreeTerm term = freeTermService.getTerm(termId);
        Visit visit = freeTermService.bookTerm(patient, term);

        VisitDto visitDto = visitMapper.mapToDto(visit);
        model.addAttribute("visit", visitDto);
        return new ModelAndView("booked-successfully");
    }

    @PostMapping(ADD_FREE_TERMS)
    public ModelAndView addNewTerms(
            @ModelAttribute("freeTermDtos") FreeTermDtos freeTermDtos,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Doctor doctor = doctorService.findDoctorByClinicUserId(userService.getUserId(username));

        List<FreeTerm> freeTerms = freeTermDtos.getFreeTermDtos().stream()
                .filter(term -> !term.getDate().toString().isBlank() || !term.getTime().isBlank())
                .map(freeTermMapper::mapFromDto).toList();
        freeTerms.forEach((a) -> a.setDoctor(doctor));

        freeTermService.addFreeTerms(freeTerms);

        return new ModelAndView("added-successfully");
    }

    @GetMapping(ADD_FREE_TERMS_FORM)
    public ModelAndView addFreeTermsForm(Model model) {
        List<FreeTermDto> freeTermDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            freeTermDtoList.add(FreeTermDto.builder().build());
        }
        FreeTermDtos freeTermDtos = new FreeTermDtos(freeTermDtoList);

        model.addAttribute("freeTermDtos", freeTermDtos);
        return new ModelAndView("add-free-terms-form");
    }

    private Patient preparePatient(PatientDto patientDto) {
        if (checkExistanceOfPatient(patientDto)) {
            return patientService.findPatientByPesel(patientDto.getPesel());
        } else {
            return patientService.addPatient(patientMapper.mapFromDto(patientDto));
        }
    }

    private boolean checkExistanceOfPatient(PatientDto patientDto) {
        return patientDto.getName().isBlank() && patientDto.getSurname().isBlank();
    }

    private List<FreeTermDto> getTermsBySpecialization(String specialization) {
        return freeTermService.getTermsBySpecialization(specialization)
                .stream().map(freeTermMapper::mapToDto).toList();
    }


}
