package pl.zajavka.infrastructure.db.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.domain.Doctor;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.DoctorEntity;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.repository.jpa.DoctorJpaRepository;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.DoctorEntityMapper;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermEntityMapper;
import pl.zajavka.util.EntityFixtures;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static pl.zajavka.util.DomainFixtures.*;

@ExtendWith(MockitoExtension.class)
public class DoctorRepositoryTest {

    @Mock
    private FreeTermEntityMapper freeTermEntityMapper;

    @Mock
    private DoctorEntityMapper doctorEntityMapper;

    @Mock
    private FreeTermJpaRepository freeTermJpaRepository;

    @Mock
    private DoctorJpaRepository doctorJpaRepository;

    @InjectMocks
    private DoctorRepository doctorRepository;



    @Test
    public void saveAllTermsCanSaveCorrectly() {
        //given
        List<FreeTerm> freeTerms = List.of(someTerm1(), someTerm2());
        Doctor doctor = someDoctor1().withFreeTerms(Set.copyOf(freeTerms));

        DoctorEntity doctorEntity = EntityFixtures.someDoctor1();
        List<FreeTermEntity> freeTermEntities = List.of(EntityFixtures.someTerm1(),EntityFixtures.someTerm2());

        //when
        Mockito.when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);
        Mockito.when(freeTermEntityMapper.mapToEntity(freeTerms.get(0))).thenReturn(freeTermEntities.get(0));
        Mockito.when(freeTermEntityMapper.mapToEntity(freeTerms.get(1))).thenReturn(freeTermEntities.get(1));
        Mockito.when(freeTermJpaRepository.saveAllAndFlush(anyList())).thenReturn(freeTermEntities);
        Mockito.when(freeTermEntityMapper.mapFromEntity(freeTermEntities.get(0))).thenReturn(freeTerms.get(0));
        Mockito.when(freeTermEntityMapper.mapFromEntity(freeTermEntities.get(1))).thenReturn(freeTerms.get(1));

        List<FreeTerm> savedTerms = doctorRepository.saveAllTerms(doctor);

        //then
        assertThat(savedTerms).hasSize(2);
    }

    @Test
    public void saveDoctorCanSaveCorrectly() {
        //given
        Doctor doctor = someDoctor1();
        DoctorEntity doctorEntity = EntityFixtures.someDoctor1().withPesel(doctor.getPesel());

        //when
        Mockito.when(doctorEntityMapper.mapToEntity(doctor)).thenReturn(doctorEntity);
        Mockito.when(doctorJpaRepository.saveAndFlush(doctorEntity)).thenReturn(doctorEntity);
        Mockito.when(doctorEntityMapper.mapFromEntity(doctorEntity)).thenReturn(doctor);

        Doctor savedDoctor = doctorRepository.saveDoctor(doctor);

        //then
        assertThat(savedDoctor).isNotNull();
        assertThat(savedDoctor).isEqualTo(doctor);
        verify(doctorJpaRepository).saveAndFlush(doctorEntity);
    }

    @Test
    public void findDoctorByClinicUserIdCanFindCorrectly() {
        //given
        Integer userId = 123;
        DoctorEntity doctorEntity = EntityFixtures.someDoctor1().withClinicUserId(userId);
        Doctor doctor = someDoctor1().withPesel(doctorEntity.getPesel());

        //when
        Mockito.when(doctorJpaRepository.findByClinicUserId(userId)).thenReturn(Optional.of(doctorEntity));
        Mockito.when(doctorEntityMapper.mapFromEntity(doctorEntity)).thenReturn(doctor);

        Doctor foundDoctor = doctorRepository.findDoctorByClinicUserId(userId);

        //then
        assertThat(foundDoctor).isNotNull();
        assertThat(foundDoctor.getPesel()).isEqualTo(doctorEntity.getPesel());
        verify(doctorJpaRepository).findByClinicUserId(userId);
    }
    @Test
    public void findDoctorByClinicUserIdShouldThrowWhenCantFound(){
        //given
        Integer userId = 1;
        String message = "Doctor with user id [%s] not found".formatted(userId);

        //when
        Mockito.when(doctorJpaRepository.findByClinicUserId(userId)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> doctorRepository.findDoctorByClinicUserId(userId));
        //then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}

