package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.zajavka.api.dto.FormNFZDto;
import pl.zajavka.api.dto.TermFromNFZDto;
import pl.zajavka.api.dto.mapper.FormNFZMapper;
import pl.zajavka.business.NFZService;
import pl.zajavka.domain.TermFromNfz;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NFZController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NFZControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private NFZService nfzService;

    @MockBean
    private FormNFZMapper formNFZMapper;

    @Test
    public void selectBenefitCanShowViewCorrectly() throws Exception {
        //when, then
        mockMvc.perform(get(NFZController.NFZ + NFZController.SELECT_BENEFIT))
                .andExpect(status().isOk())
                .andExpect(view().name("select-benefit"));
    }

    @Test
    public void getTermFormCanShowViewCorrectly() throws Exception {
        //given
        List<String> someBenefitsList = Stream.of("Benefit1", "Benefit2").collect(Collectors.toList());

        //when, then
        Mockito.when(nfzService.getAvailableBenefits(Mockito.any())).thenReturn(someBenefitsList);

        mockMvc.perform(get(NFZController.NFZ + NFZController.GET_TERM_FORM)
                        .param("benefit", "someBenefit"))
                .andExpect(status().isOk())
                .andExpect(view().name("get-term-form"))
                .andExpect(model().attributeExists("formAttributes", "voivodeships", "availableBenefits", "cases"));
    }

    @Test
    public void getTermCanShowViewCorrectly() throws Exception {
        //given
        FormNFZDto formAttributes = new FormNFZDto();
        TermFromNfz firstTerm = new TermFromNfz();
        TermFromNFZDto termDto = new TermFromNFZDto();

        //when, then
        Mockito.when(nfzService.getFirstTerm(Mockito.any())).thenReturn(firstTerm);

        Mockito.when(formNFZMapper.mapToDto(Mockito.any())).thenReturn(termDto);

        mockMvc.perform(get(NFZController.NFZ + NFZController.GET_TERM)
                        .flashAttr("formAttributes", formAttributes))
                .andExpect(status().isOk())
                .andExpect(view().name("first-available-term"))
                .andExpect(model().attributeExists("termDto"));
    }
}
