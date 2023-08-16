package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.zajavka.api.dto.DiseaseHistoryDto;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.PatientMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.PatientService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.DiseaseHistory;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.List;

@Controller
@AllArgsConstructor
public class PatientController {
    public static final String PATIENT_PANEL = "/patient-panel";

    public static final String VISIT_DETAILS = "/visit-details/{visitId}";

    public static final String FINISHED_VISITS = "/finished-visits/{pesel}";
    public static final String UPCOMING_VISITS = "/upcoming-visits/{pesel}";
    public static final String CANCEL_VISIT = "/cancel-visit/{visitId}";
    public static final String PATIENT_HISTORY = "/patient-history/{visitId}";

    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping(PATIENT_PANEL)
    public ModelAndView showPatientPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        Patient patient = patientService.findPatientByClinicUsername(username);

        PatientDto patientDto = patientMapper.mapToDto(patient);
        model.addAttribute("patientDto", patientDto);


        return new ModelAndView("patient-panel");
    }

    @GetMapping(FINISHED_VISITS)
    public ModelAndView showFinishedVisits(@PathVariable String pesel, Model model) {
        Patient patient = patientService.findPatientByPesel(pesel);
        List<Visit> visits = visitService.getListVisit(patient, Visit.Status.DONE);
        List<VisitDto> visitDtos = visits.stream().map(visitMapper::mapToDto).toList();

        model.addAttribute("visitDtos", visitDtos);

        return new ModelAndView("finished-visits");
    }

    @GetMapping(UPCOMING_VISITS)
    public ModelAndView showUpcomingVisits(@PathVariable String pesel, Model model) {
        Patient patient = patientService.findPatientByPesel(pesel);
        List<Visit> visits = visitService.getListVisit(patient, Visit.Status.UPCOMING);
        List<VisitDto> visitDtos = visits.stream().map(visitMapper::mapToDto).toList();

        model.addAttribute("visitDtos", visitDtos);

        return new ModelAndView("upcoming-visits");
    }

    @GetMapping(VISIT_DETAILS)
    public ModelAndView showVisitDetails(@PathVariable String visitId, Model model) {
        Visit visit = visitService.findVisitById(Integer.valueOf(visitId));
        VisitDto visitDto = visitMapper.mapToDto(visit);

        model.addAttribute("visitDto", visitDto);

        return new ModelAndView("visit-details");
    }

    @PutMapping(CANCEL_VISIT)
    public ModelAndView cancelVisit(@PathVariable String visitId, Model model) {
        Visit visit = visitService.cancelVisit(Integer.valueOf(visitId));
        VisitDto visitDto = visitMapper.mapToDto(visit);

        model.addAttribute("visitDto", visitDto);

        return new ModelAndView("cancelled-successfully");
    }

    @GetMapping(PATIENT_HISTORY)
    public ModelAndView showPatientHistory(@PathVariable String visitId, Model model) {
        Patient patient = patientService.findPatientByVisitId(Integer.valueOf(visitId));
        DiseaseHistory diseaseHistory = patientService.showDiseaseHistory(patient);
        DiseaseHistoryDto diseaseHistoryDto = patientMapper.mapDiseaseHistory(diseaseHistory);

        model.addAttribute("diseaseHistoryDto", diseaseHistoryDto);

        return new ModelAndView("disease-history");

    }

}
