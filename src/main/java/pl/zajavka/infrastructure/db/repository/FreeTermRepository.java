package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.FreeTermDao;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class FreeTermRepository implements FreeTermDao {
    private final FreeTermJpaRepository freeTermJpaRepository;
    private final FreeTermEntityMapper freeTermEntityMapper;
    @Override
    public void saveAll(List<FreeTerm> freeTerms) {
        List<FreeTermEntity> freeTermEntities = freeTerms.stream().map(freeTermEntityMapper::mapToEntity).toList();
        freeTermJpaRepository.saveAll(freeTermEntities);
    }

    @Override
    public boolean checkAvailabilityOfTerm(Integer id) {
        return freeTermJpaRepository.findById(id).isPresent();

    }

    @Override
    public void delete(FreeTerm freeTerm) {
        FreeTermEntity freeTermEntity = freeTermEntityMapper.mapToEntity(freeTerm);
        freeTermJpaRepository.delete(freeTermEntity);
    }

    @Override
    public List<FreeTerm> getTermsBySpecialization(String specialization) {
        return freeTermJpaRepository.getFreeTermsBySpecialization(specialization).stream()
                .map(freeTermEntityMapper::mapFromEntity)
                .toList();

    }

    @Override
    public FreeTerm getTerm(Integer freeTermId) {
        return freeTermEntityMapper
                .mapFromEntity(
                        freeTermJpaRepository
                                .findById(freeTermId)
                                .orElseThrow(()-> new NotFoundException("Term with id [%d) not found"
                                        .formatted(freeTermId))));
    }


}
