package pl.zajavka.infrastructure.db.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.zajavka.infrastructure.db.entity.VisitEntity;
import pl.zajavka.integration.configuration.PersistenceContainerTestConfiguration;

import java.util.List;

import static pl.zajavka.util.EntityFixtures.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class VisitJpaRepositoryTest {

    private final VisitJpaRepository visitJpaRepository;

    @Test
    void visitCanBeSavedAndUpdateCorrectly() {
        //given
        VisitEntity visitEntity = someVisit1();
        VisitEntity saved = visitJpaRepository.saveAndFlush(visitEntity);
        VisitEntity visitEntityWithDescription = saved.withDescription("someDescription");

        //when
        VisitEntity savedWithDescription = visitJpaRepository.saveAndFlush(visitEntityWithDescription);
        VisitEntity savedById = visitJpaRepository.findById(saved.getId()).get();
        VisitEntity savedWithDescriptionById = visitJpaRepository.findById(savedWithDescription.getId()).get();

        //then

        Assertions.assertEquals(savedById, savedWithDescriptionById);
    }

    @Test
    void visitsCanBeFoundByPatientId(){
        //given
        List<VisitEntity> visitEntities = visitJpaRepository.saveAll(List.of(someVisit1(), someVisit2()));
        //when
        List<VisitEntity> byPatientId = visitJpaRepository.findByPatientId(1);
        //then
        Assertions.assertEquals(3, byPatientId.size());
    }
}