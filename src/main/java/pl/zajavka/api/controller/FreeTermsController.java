package pl.zajavka.api.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.FreeTermMapper;
import pl.zajavka.api.dto.mapper.PatientMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.FreeTermService;
import pl.zajavka.business.PatientService;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.List;

@Controller
@RequestMapping()
@AllArgsConstructor
public class FreeTermsController {

    public static final String FREE_TERMS = "/free-terms";
    public static final String SELECT_SPEC = "/select-specialization";
    public static final String PREPARE_BOOKING = "/book-term/{freeTermId}";
    public static final String BOOK_TERM = "/book-term";
    private final DoctorService doctorService;
    private final FreeTermService freeTermService;
    private final FreeTermMapper freeTermMapper;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final VisitMapper visitMapper;

    @GetMapping(value = SELECT_SPEC)
    public ModelAndView selectSpecialization(Model model){
        List<String> specializations = doctorService.getSpecializations();
        model.addAttribute("specializations", specializations);

        return new ModelAndView("select-specialization");
    }

    @GetMapping(FREE_TERMS)
    public ModelAndView showFreeTerms(@RequestParam String specialization, Model model){
        List<FreeTermDto> freeTerms = getTermsBySpecialization(specialization);
        model.addAttribute("freeTermDtos", freeTerms);
        return new ModelAndView("free-terms-list");
    }

    @GetMapping(PREPARE_BOOKING)
    public ModelAndView prepareBookingTerm(@PathVariable Integer freeTermId,Model model){
        model.addAttribute("term", freeTermMapper.mapToDto(freeTermService.getTerm(freeTermId)));
        model.addAttribute("patientDto", PatientDto.builder().build());
        return new ModelAndView("book-term");
    }

    @PostMapping(BOOK_TERM)
    public ModelAndView bookTerm(
        @Valid @ModelAttribute("patientDto") PatientDto patientDto,
        @RequestParam("termId") Integer termId,
        Model model
    ){
        Patient patient = preparePatient(patientDto);

        FreeTerm term = freeTermService.getTerm(termId);
        Visit visit = freeTermService.bookTerm(patient, term);

        VisitDto visitDto = visitMapper.mapToDto(visit);
        model.addAttribute("visit", visitDto);
        return new ModelAndView("booked-successfully");

    }

    private Patient preparePatient(PatientDto patientDto) {
        if (checkExistanceOfPatient(patientDto)){
            return patientService.findPatientByPesel(patientDto.getPesel());
        }else {
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
