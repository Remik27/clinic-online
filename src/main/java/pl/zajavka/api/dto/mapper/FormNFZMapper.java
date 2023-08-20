package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.FormNFZDto;
import pl.zajavka.api.dto.TermFromNFZDto;
import pl.zajavka.domain.Case;
import pl.zajavka.domain.FormNfz;
import pl.zajavka.domain.TermFromNfz;
import pl.zajavka.domain.Voivodeships;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.Arrays;

@Mapper(componentModel = "spring")
public interface FormNFZMapper {
    default FormNfz map(FormNFZDto form){
        return FormNfz.builder()
                .benefit(form.getBenefit())
                .voivodeshipId(
                        Arrays
                                .stream(Voivodeships.values())
                                .filter(voivodeship->voivodeship.getName().equals(form.getVoivodeship()))
                                .map(Voivodeships::getId)
                                .findFirst().orElseThrow(()->new NotFoundException("Voivodeship not found")))
                .location(form.getLocation())
                .priorityId(Case.valueOf(form.getPriority()).getId())
                .forChildren(form.getForChildren())
                .build();
    }
    TermFromNFZDto mapToDto(TermFromNfz term);
}
