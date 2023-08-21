package pl.zajavka.infrastructure.db.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.zajavka.domain.Patient;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.PatientEntity;
import pl.zajavka.infrastructure.db.repository.jpa.PatientJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.PatientEntityMapper;
import pl.zajavka.util.DomainFixtures;
import pl.zajavka.util.EntityFixtures;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class PatientRepositoryTest {
    @Mock
    private PatientJpaRepository patientJpaRepository;

    @Mock
    private PatientEntityMapper patientEntityMapper;

    @InjectMocks
    private PatientRepository patientRepository;

    @Test
    public void findPatientCanReturnPatientCorrectly() {
        //given
        Integer id = 1;
        PatientEntity patientEntity = EntityFixtures.somePatient().withId(id);
        Patient patient = DomainFixtures.somePatient().withId(id);

        //when
        Mockito.when(patientJpaRepository.findById(id)).thenReturn(Optional.of(patientEntity));
        Mockito.when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        Patient result = patientRepository.findPatient(patient);

        //then
        assertThat(result).isEqualTo(patient);
    }

    @Test
    public void findPatientShouldThrowWhenNotFound() {
        //given
        Integer id = 1;
        String message = "Patient [%d] not found".formatted(id);
        Patient patient = DomainFixtures.somePatient().withId(id);

        //when
        Mockito.when(patientJpaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> patientRepository.findPatient(patient));
        //then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    public void addPatientCanAddCorrectly() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        Patient patient = DomainFixtures.somePatient();

        //when
        Mockito.when(patientEntityMapper.mapToEntity(patient)).thenReturn(patientEntity);
        Mockito.when(patientJpaRepository.saveAndFlush(patientEntity)).thenReturn(patientEntity);
        Mockito.when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        Patient result = patientRepository.addPatient(patient);

        //then
        assertThat(result).isEqualTo(patient);
    }

    @ParameterizedTest
    @MethodSource("checkExistanceData")
    public void checkExistencePatientCanCheckCorrectly(Patient patient, String pesel, boolean expect) {

        //when
        if (patient.getPesel().equals(pesel)) {
            Mockito.when(patientJpaRepository.findByPesel(pesel)).thenReturn(Optional.of(EntityFixtures.somePatient()));
        } else {
            Mockito.when(patientJpaRepository.findByPesel(patient.getPesel())).thenReturn(Optional.empty());
        }

        boolean result = patientRepository.checkExistencePatient(patient);

        //then
        assertThat(result).isEqualTo(expect);
    }

    public static Stream<Arguments> checkExistanceData() {
        return Stream.of(
                Arguments.of(DomainFixtures.somePatient().withPesel("11111111111"), "11111111111", true),
                Arguments.of(DomainFixtures.somePatient().withPesel("11111111111"), "22222222222", false)
        );
    }

    @Test
    public void findPatientByPeselCanReturnCorrectly() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        Patient patient = DomainFixtures.somePatient();
        //when
        Mockito.when(patientJpaRepository.findByPesel(patient.getPesel())).thenReturn(Optional.of(patientEntity));
        Mockito.when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        Patient result = patientRepository.findPatientByPesel(patient.getPesel());
//then
        assertThat(result).isEqualTo(patient);
    }

    @Test
    public void findPatientByPeselShouldThrowWhenNotFound() {
        //given
        String pesel = "11111111111";
        String message = "Patient with pesel[%s] not found".formatted(pesel);

        //when
        Mockito.when(patientJpaRepository.findByPesel(pesel)).thenReturn(Optional.empty());

        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> patientRepository.findPatientByPesel(pesel));
        //then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    public void findPatientByClinicUserIdCanReturnPatientCorrectly() {
        //given
        Integer id = 1;
        PatientEntity patientEntity = EntityFixtures.somePatient().withClinicUserId(id);
        Patient patient = DomainFixtures.somePatient().withClinicUserId(id);

        //when
        Mockito.when(patientJpaRepository.findByClinicUserId(id)).thenReturn(Optional.of(patientEntity));
        Mockito.when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        Patient result = patientRepository.findPatientByClinicUserId(id);

        //then
        assertThat(result).isEqualTo(patient);
    }

    @Test
    public void findPatientByClinicUserIdShouldThrowWhenNotFound() {
        //given
        Integer id = 1;
        String message = "Patient with user id [%s] not found".formatted(id);

        //when
        Mockito.when(patientJpaRepository.findByClinicUserId(id)).thenReturn(Optional.empty());

        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> patientRepository.findPatientByClinicUserId(id));
        //then
        assertThat(exception.getMessage()).isEqualTo(message);
    }

    @Test
    public void findPatientByVisitIdCanReturnPatientCorrectly() {
        //given
        Integer id = 1;
        PatientEntity patientEntity = EntityFixtures.somePatient();
        Patient patient = DomainFixtures.somePatient();

        //when
        Mockito.when(patientJpaRepository.findPatientByVisitId(id)).thenReturn(patientEntity);
        Mockito.when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        Patient result = patientRepository.findPatientByVisitId(id);

        //then
        assertThat(result).isEqualTo(patient);
    }

    @Test
    public void updatePatientCanUpdateCorrectly() {
        //given
        PatientEntity patientEntity = EntityFixtures.somePatient();
        Patient patient = DomainFixtures.somePatient();
        //when
        Mockito.when(patientEntityMapper.mapToEntity(patient)).thenReturn(patientEntity);
        Mockito.when(patientJpaRepository.saveAndFlush(patientEntity)).thenReturn(patientEntity);
        Mockito.when(patientEntityMapper.mapFromEntity(patientEntity)).thenReturn(patient);

        Patient result = patientRepository.updatePatient(patient);

        //then
        assertThat(result).isEqualTo(patient);
    }
}
