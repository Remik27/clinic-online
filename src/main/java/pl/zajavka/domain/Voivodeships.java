package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Voivodeships {
    DOLNOSLASKIE("DOLNOŚLĄSKIE", "01"),
    KUJAWSKO_POMORSKIE("KUJAWSKO POMORSKIE", "02"),
    LUBELSKIE("LUBELSKIE", "03"),
    LUBUSKIE("LUBUSKIE", "04"),
    LODZKIE("ŁÓDZKIE", "05"),
    MALOPOLSKIE("MAŁOPOLSKIE", "06"),
    MAZOWIECKIE("MAZOWIECKIE", "07"),
    OPOLSKIE("OPOLSKIE", "08"),
    PODKARPACKIE("PODKARPACKIE", "09"),
    PODLASKIE("PODLASKIE", "10"),
    POMORSKIE("POMORSKIE", "11"),
    SLASKIE("ŚLĄSKIE", "12"),
    SWIETOKRZYSKIE("ŚWIĘTOKRZYSKIE", "13"),
    WARMINSKO_MAZURSKIE("WARMIŃSKO MAZURSKIE", "14"),
    WIELKOPOLSKIE("WIELKOPOLSKIE", "15"),
    ZACHODNIOPOMORSKIE("ZACHODNIOPOMORSKIE", "16"),
    ;
    private final String name;
    private final String id;

}
