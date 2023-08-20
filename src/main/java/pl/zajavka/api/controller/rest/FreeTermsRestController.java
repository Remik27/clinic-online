package pl.zajavka.api.controller.rest;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.FreeTermsDtos;
import pl.zajavka.api.dto.PatientDto;
import pl.zajavka.api.dto.SpecializationsDtos;
import pl.zajavka.api.dto.VisitDto;
import pl.zajavka.api.dto.mapper.FreeTermMapper;
import pl.zajavka.api.dto.mapper.PatientMapper;
import pl.zajavka.api.dto.mapper.VisitMapper;
import pl.zajavka.business.DoctorService;
import pl.zajavka.business.FreeTermService;
import pl.zajavka.business.PatientService;
import pl.zajavka.business.VisitService;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;

import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(FreeTermsRestController.FREE_TERMS_API)
public class FreeTermsRestController {
    public static final String FREE_TERMS_API = "/api/free-terms";
    public static final String GET_FREE_TERMS = "/free-terms/{specialization}";
    public static final String GET_SPECIALIZATIONS = "/get-specializations";
    public static final String BOOK_TERM = "/book-term/{termId}";
    private final FreeTermService freeTermService;
    private final FreeTermMapper freeTermMapper;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final PatientMapper patientMapper;
    private final VisitService visitService;
    private final VisitMapper visitMapper;

    @GetMapping(GET_FREE_TERMS)
    public ResponseEntity<FreeTermsDtos> getFreeTerms(@PathVariable String specialization) {
        if (checkSpecializationExist(specialization)) {
            return ResponseEntity.badRequest().build();
        }
        List<FreeTerm> terms = freeTermService.getTermsBySpecialization(specialization);
        FreeTermsDtos freeTermsDtos = FreeTermsDtos.builder()
                .freeTermDtos(terms.stream().map(freeTermMapper::mapToDto).toList())
                .build();
        return ResponseEntity.ok(freeTermsDtos);
    }

    @GetMapping(GET_SPECIALIZATIONS)
    public SpecializationsDtos getSpecializations() {
        return SpecializationsDtos.builder()
                .specializations(doctorService.getSpecializations())
                .build();
    }

    @PostMapping(BOOK_TERM)
    public VisitDto bookTerm(
            @PathVariable Integer termId,
            @Valid @RequestBody PatientDto patientDto) {


        Patient patient = getPatient(patientDto);
        FreeTerm term = freeTermService.getTerm(termId);
        Visit visit = visitService.buildVisit(patient, term);
        return visitMapper.mapToDto(visit);

    }

    private Patient getPatient(PatientDto patientDto) {
        if (patientService.checkExistencePatient(patientMapper.mapFromDto(patientDto))) {
            return patientService.findPatientByPesel(patientDto.getPesel());
        } else {
            return patientService.addPatient(patientMapper.mapFromDto(patientDto));
        }
    }

    private boolean checkSpecializationExist(String specialization) {
        return Arrays.stream(Doctor.Specialization.values()).noneMatch(a -> a.name().equals(specialization));
    }


}
