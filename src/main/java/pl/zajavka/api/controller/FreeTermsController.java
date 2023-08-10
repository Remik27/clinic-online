package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.FreeTermDto;
import pl.zajavka.api.dto.mapper.FreeTermMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.FreeTermService;

import java.util.List;

@Controller
@RequestMapping()
@AllArgsConstructor
public class FreeTermsController {

    public static final String FREE_TERMS = "/free-terms";
    public static final String SELECT_SPEC = "/select-specialization";
    private final DoctorService doctorService;
    private final FreeTermService freeTermService;
    private final FreeTermMapper freeTermMapper;

    @GetMapping(value = SELECT_SPEC)
    public ModelAndView selectSpecialization(Model model){
        List<String> specializations = doctorService.getSpecializations();
        model.addAttribute("specializations", specializations);

        return new ModelAndView("select-specialization");
    }

    @PostMapping(FREE_TERMS)
    public ModelAndView showFreeTerms(@RequestParam String specialization, Model model){
        List<FreeTermDto> freeTerms = getTermsBySpecialization(specialization);
        model.addAttribute("freeTermDtos", freeTerms);
        return new ModelAndView("free-terms-list");
    }

    private List<FreeTermDto> getTermsBySpecialization(String specialization) {
        return freeTermService.getTermsBySpecialization(specialization)
                .stream().map(freeTermMapper::mapToDto).toList();
    }


}
