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
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermEntityMapper;
import pl.zajavka.util.EntityFixtures;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static pl.zajavka.util.DomainFixtures.someTerm1;
import static pl.zajavka.util.DomainFixtures.someTerm2;

@ExtendWith(MockitoExtension.class)
public class FreeTermRepositoryTest {
    @Mock
    private FreeTermJpaRepository freeTermJpaRepository;

    @Mock
    private FreeTermEntityMapper freeTermEntityMapper;

    @InjectMocks
    private FreeTermRepository freeTermRepository;



    @Test
    public void saveAllCanSaveCorrectly() {
        // given
        FreeTerm freeTerm1 = someTerm1();
        FreeTerm freeTerm2 = someTerm2();
        FreeTermEntity freeTermEntity1 = EntityFixtures.someTerm1();
        FreeTermEntity freeTermEntity2 = EntityFixtures.someTerm2();
        List<FreeTerm> freeTermList = List.of(freeTerm1, freeTerm2);
        List<FreeTermEntity> freeTermEntities = List.of(freeTermEntity1, freeTermEntity2);

        //when
        Mockito.when(freeTermEntityMapper.mapToEntity(freeTerm1)).thenReturn(freeTermEntity1);
        Mockito.when(freeTermEntityMapper.mapToEntity(freeTerm2)).thenReturn(freeTermEntity2);
        Mockito.when(freeTermJpaRepository.saveAll(eq(freeTermEntities))).thenReturn(freeTermEntities);
        Mockito.when(freeTermEntityMapper.mapFromEntity(freeTermEntity1)).thenReturn(freeTerm1);
        Mockito.when(freeTermEntityMapper.mapFromEntity(freeTermEntity2)).thenReturn(freeTerm2);

        List<FreeTerm> savedTerms = freeTermRepository.saveAll(freeTermList);

        // then
        assertThat(savedTerms).hasSize(2);
        verify(freeTermJpaRepository).saveAll(anyList());
    }

    @ParameterizedTest
    @MethodSource("isAvailabilityData")
    public void checkAvailabilityOfTermCanCheckCorrectly(Integer termId, boolean expect) {
        //given
        Integer availableId = 1;
        FreeTermEntity freeTermEntity = EntityFixtures.someTerm1().withId(termId);

        //when
        if (availableId.equals(termId)) {
            Mockito.when(freeTermJpaRepository.findById(availableId)).thenReturn(Optional.of(freeTermEntity));
        }
        else {
            Mockito.when(freeTermJpaRepository.findById(termId)).thenReturn(Optional.empty());
        }

        boolean isAvailable = freeTermRepository.checkAvailabilityOfTerm(termId);

        // then
        assertThat(isAvailable).isEqualTo(expect);
    }

    public static Stream<Arguments> isAvailabilityData(){
        return Stream.of(
                Arguments.of(1, true),
                Arguments.of(2, false)
        );
    }

    @Test
    public void deleteCanDeleteCorrectly() {
        // given
        FreeTerm freeTerm = someTerm1();
        FreeTermEntity freeTermEntity = EntityFixtures.someTerm1();

        //when
        Mockito.when(freeTermEntityMapper.mapToEntity(freeTerm)).thenReturn(freeTermEntity);

        freeTermRepository.delete(freeTerm);

        // then
        verify(freeTermJpaRepository).delete(freeTermEntity);
    }

    @Test
    public void getTermsBySpecializationCanReturnTermsCorrectly() {
        // given
        String specialization = "OKULISTA";
        List<FreeTermEntity> entities = List.of(EntityFixtures.someTerm1(), EntityFixtures.someTerm2());
        List<FreeTerm> freeTerms = List.of(someTerm1());

        //when
        Mockito.when(freeTermJpaRepository.getFreeTermsBySpecialization(specialization))
                .thenReturn(List.of(entities.get(0)));
        Mockito.when(freeTermEntityMapper.mapFromEntity(entities.get(0))).thenReturn(freeTerms.get(0));

        List<FreeTerm> terms = freeTermRepository.getTermsBySpecialization(specialization);

        // then
        assertThat(terms).hasSize(1);
        assertThat(terms.get(0)).isEqualTo(freeTerms.get(0));
    }

    @Test
    public void getTermCanReturnTermCorrectly() {
        // given
        Integer id = 1;
        FreeTermEntity freeTermEntity = EntityFixtures.someTerm1();
        FreeTerm freeTerm = someTerm1();

        //when
        Mockito.when(freeTermJpaRepository.findById(id)).thenReturn(Optional.of(freeTermEntity));
        Mockito.when(freeTermEntityMapper.mapFromEntity(freeTermEntity)).thenReturn(freeTerm);

        FreeTerm term = freeTermRepository.getTerm(id);

        // then
        assertThat(term).isEqualTo(freeTerm);
    }
    @Test
    public void getTermShouldThrowWhenNotFouund() {
        // given
        Integer id = 1;
        String message = "Term with id [%d) not found".formatted(id);

        //when
        Mockito.when(freeTermJpaRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> freeTermRepository.getTerm(id));

        // then
        assertThat(exception.getMessage()).isEqualTo(message);
    }
}

