package pl.zajavka.api.controller.rest;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.VisitsDtos;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.PatientService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(VisitRestController.VISIT_API)
public class VisitRestController {
    public static final String VISIT_API = "/api/visit";
    private static final String CANCEL_VISIT = "/cancel-visit/{visitId}";
    private static final String GET_VISITS_BY_PESEL = "/get-visits/{pesel}";
    private static final String DELETE_CANCELLED_VISIT = "/delete-cancelled-visits";
    private final VisitService visitService;
    private final VisitMapper visitMapper;
    private final PatientService patientService;

    @PutMapping(CANCEL_VISIT)
    public VisitDto cancelVisit(@PathVariable Integer visitId){
        Visit visit = visitService.cancelVisit(visitId);
        return visitMapper.mapToDto(visit);
    }

    @GetMapping(GET_VISITS_BY_PESEL)
    public VisitsDtos getVisits(@Size(min = 11, max = 11, message = "pesel") @PathVariable String pesel){
        Patient patient = patientService.findPatientByPesel(pesel);
        return VisitsDtos.builder().visits(visitService.getListVisit(patient, Visit.Status.UPCOMING)
                .stream()
                .map(visitMapper::mapToDto)
                .toList())
                .build();
    }
    @DeleteMapping(DELETE_CANCELLED_VISIT)
    public Integer deleteCancelledVisits(){
        List<Visit> visits = visitService.findVisitsByStatus(Visit.Status.CANCELLED);
        Integer deletedElements = visits.size();
        visitService.delete(visits);
        return deletedElements;
    }
}
