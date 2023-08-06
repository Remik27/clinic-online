package pl.zajavka.infrastructure.db.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;

import static pl.zajavka.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class FreeTermJpaRepositoryTest {

    private FreeTermJpaRepository freeTermJpaRepository;

    @Test
    void freeTermsCanBeSavedCorrectly() {
        //given
        List<FreeTermEntity> terms = List.of(someTerm1(), someTerm2(), someTerm3());
        freeTermJpaRepository.saveAll(terms);

        //when
        List<FreeTermEntity> savedTerms = freeTermJpaRepository.findAll();

        //then

        Assertions.assertEquals(terms.size(), savedTerms.size());


    }

}