package pl.zajavka.infrastructure.db.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.Visit;
import pl.zajavka.infrastructure.db.entity.VisitEntity;
import pl.zajavka.infrastructure.db.repository.jpa.VisitJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.VisitEntityMapper;
import pl.zajavka.util.DomainFixtures;
import pl.zajavka.util.EntityFixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class VisitRepositoryTest {
    @Mock
    private VisitJpaRepository visitJpaRepository;

    @Mock
    private VisitEntityMapper visitEntityMapper;

    @InjectMocks
    private VisitRepository visitRepository;

    @Test
    public void updateVisitCanUpdateCorrectly() {
        // given
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        Visit visit = DomainFixtures.someVisit1();
        //when
        Mockito.when(visitEntityMapper.mapToEntity(visit)).thenReturn(visitEntity);
        Mockito.when(visitJpaRepository.saveAndFlush(visitEntity)).thenReturn(visitEntity);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        Visit result = visitRepository.updateVisit(visit);

        // then
        assertThat(result).isEqualTo(visit);
    }

    @Test
    public void findByIdCanReturnVisitCorrectly() {
        // given
        Integer id = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        Visit visit = DomainFixtures.someVisit1();

        //when
        Mockito.when(visitJpaRepository.findById(id)).thenReturn(Optional.of(visitEntity));
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        Visit result = visitRepository.findById(id);

        // then
        assertThat(result).isEqualTo(visit);
    }

    @Test
    public void findVisitsByPatientIdCanReturnVisitsCorrectly() {
        // given
        Integer patientId = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        Visit visit = DomainFixtures.someVisit1();
        List<VisitEntity> entities = new ArrayList<>(List.of(visitEntity));

        //when
        Mockito.when(visitJpaRepository.findByPatientId(patientId)).thenReturn(entities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        List<Visit> result = visitRepository.findVisitsByPatientId(patientId);

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void findListDoneVisitCanReturnVisitsCorrectly() {
        //given
        Integer id = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        List<VisitEntity> visitEntities = new ArrayList<>(List.of(visitEntity));
        Visit visit = DomainFixtures.someVisit1();
        Patient patient = DomainFixtures.somePatient().withId(id);

        //when
        Mockito.when(visitJpaRepository.getListDoneVisit(id)).thenReturn(visitEntities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        List<Visit> result = visitRepository.getListDoneVisit(patient);

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void findListUpcomingVisitCanReturnVisitsCorrectly() {
        //given
        Integer id = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        List<VisitEntity> visitEntities = new ArrayList<>(List.of(visitEntity));
        Visit visit = DomainFixtures.someVisit1();
        Patient patient = DomainFixtures.somePatient().withId(id);

        //when
        Mockito.when(visitJpaRepository.getListUpcomingVisit(id)).thenReturn(visitEntities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        List<Visit> result = visitRepository.getListUpcomingVisit(patient);

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void findUpcomingVisitsByDoctorIdCanReturnVisitsCorrectly() {
        //given
        Integer id = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        List<VisitEntity> visitEntities = new ArrayList<>(List.of(visitEntity));
        Visit visit = DomainFixtures.someVisit1();

        //when
        Mockito.when(visitJpaRepository.getListUpcomingVisitByDoctor(id)).thenReturn(visitEntities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        List<Visit> result = visitRepository.findUpcomingVisitsByDoctorId(id);

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void findDoneVisitsByDoctorIdCanReturnVisitsCorrectly() {
        Integer id = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        List<VisitEntity> visitEntities = new ArrayList<>(List.of(visitEntity));
        Visit visit = DomainFixtures.someVisit1();

        //when
        Mockito.when(visitJpaRepository.getListDoneVisitByDoctor(id)).thenReturn(visitEntities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        List<Visit> result = visitRepository.findDoneVisitsByDoctorId(id);

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void findVisitsByPatientIdAndStatusCanReturnVisitsCorrectly() {
        Integer id = 1;
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        List<VisitEntity> visitEntities = new ArrayList<>(List.of(visitEntity));
        Visit visit = DomainFixtures.someVisit1();
        Visit.Status status = Visit.Status.DONE;

        //when
        Mockito.when(visitJpaRepository.findByPatientIdAndStatus(id, status.toString())).thenReturn(visitEntities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntity)).thenReturn(visit);

        List<Visit> result = visitRepository.findVisitsByPatientIdAndStatus(id, status);

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void getListCancelledVisitsCanReturnVisitsCorrectly() {
        //given
        List<VisitEntity> visitEntities = List.of(EntityFixtures.someVisit1());
        Visit visit = DomainFixtures.someVisit1();
        Visit.Status status = Visit.Status.CANCELLED;

        //when
        Mockito.when(visitJpaRepository.findByStatus(status.toString())).thenReturn(visitEntities);
        Mockito.when(visitEntityMapper.mapFromEntity(visitEntities.get(0))).thenReturn(visit);

        List<Visit> result = visitRepository.getListCancelledVisits();

        // then
        assertThat(result).containsExactly(visit);
    }

    @Test
    public void testDelete() {
        // given
        Visit visit = DomainFixtures.someVisit1();
        VisitEntity visitEntity = EntityFixtures.someVisit1();
        List<Visit> visits = List.of(visit);
        //when
        Mockito.when(visitEntityMapper.mapToEntity(any())).thenReturn(visitEntity);

        visitRepository.delete(visits);

        //then
        verify(visitJpaRepository, times(1)).deleteAll(anyList());
    }
}


