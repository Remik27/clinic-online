package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.*;
import pl.zajavka.api.dto.mapper.FormNFZMapper;
import pl.zajavka.business.NFZService;
import pl.zajavka.domain.Case;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.domain.Voivodeships;

import java.util.stream.Stream;

@Controller
@AllArgsConstructor
@RequestMapping(NFZController.NFZ)
public class NFZController {

    public static final String NFZ = "/nfz";

    private static final String SELECT_BENEFIT = "/select-benefit";
    private static final String GET_TERM_FORM = "/get-term-form";
    private static final String GET_TERM = "/get-term";
    private final NFZService nfzService;
    private final FormNFZMapper formNFZMapper;

    @GetMapping(SELECT_BENEFIT)
    public ModelAndView selectBenefit(Model model){
        return new ModelAndView("select-benefit");
    }

    @GetMapping(GET_TERM_FORM)
    public ModelAndView getTermForm(@RequestParam String benefit,
                                    Model model){
        FormNFZDto formAttributes = new FormNFZDto();

        VoivodeshipsDto voivodeships = VoivodeshipsDto.builder()
                .voivodeships(Stream.of(Voivodeships.values()).map(Voivodeships::getName).toList())
                .build();

        BenefitsDto availableBenefits = BenefitsDto.builder().benefits(nfzService.getAvailableBenefits(benefit)).build();

        CasesDtos cases = CasesDtos.builder().cases(Stream.of(Case.values()).map(Case::name).toList()).build();

        model.addAttribute("formAttributes", formAttributes);
        model.addAttribute("voivodeships", voivodeships);
        model.addAttribute("availableBenefits", availableBenefits);
        model.addAttribute("cases", cases);



    return new ModelAndView("get-term-form");
    }

    @GetMapping(GET_TERM)
    public ModelAndView getTerm(@ModelAttribute("formAttributes") FormNFZDto formAttributes,
                                Model model){
        TermFromNfz firstTerm = nfzService.getFirstTerm(formNFZMapper.map(formAttributes));
        TermFromNFZDto termDto = formNFZMapper.mapToDto(firstTerm);
        model.addAttribute("termDto", termDto);

        return new ModelAndView("first-available-term");
    }
}
