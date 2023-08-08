package pl.zajavka.infrastructure.db.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.zajavka.infrastructure.db.entity.PatientEntity;
import pl.zajavka.integration.configuration.PersistenceContainerTestConfiguration;
import pl.zajavka.util.EntityFixtures;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientJpaRepositoryTest {

    private final PatientJpaRepository patientJpaRepository;

    @Test
    void patientCanBeFoundCorrectly(){
        //given
        PatientEntity patient = EntityFixtures.somePatient();
        PatientEntity saved = patientJpaRepository.save(patient);

        //when
        PatientEntity foundedById = patientJpaRepository.findById(saved.getId()).get();

        //then
        Assertions.assertEquals(patient, foundedById);
    }
}