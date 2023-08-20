package pl.zajavka.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Case {
    STABILNY( 1),
    PILNY( 2);
    private final Integer id;
}
