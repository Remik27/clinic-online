package pl.zajavka.infrastructure.db.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.FreeTermDao;
import pl.zajavka.domain.FreeTerm;
import pl.zajavka.infrastructure.db.entity.FreeTermEntity;
import pl.zajavka.infrastructure.db.repository.jpa.FreeTermJpaRepository;
import pl.zajavka.infrastructure.db.repository.mapper.FreeTermMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class FreeTermRepository implements FreeTermDao {
    private final FreeTermJpaRepository freeTermJpaRepository;
    private final FreeTermMapper freeTermMapper;
    @Override
    public void saveAll(List<FreeTerm> freeTerms) {
        List<FreeTermEntity> freeTermEntities = freeTerms.stream().map(freeTermMapper::mapToEntity).toList();
        freeTermJpaRepository.saveAll(freeTermEntities);
    }

    @Override
    public boolean checkAvailabilityOfTerm(Integer id) {
        return freeTermJpaRepository.findById(id).isPresent();

    }

    @Override
    public void delete(FreeTerm freeTerm) {
        FreeTermEntity freeTermEntity = freeTermMapper.mapToEntity(freeTerm);
        freeTermJpaRepository.delete(freeTermEntity);
    }


}
