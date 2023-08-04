package pl.zajavka.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Entity
@AllArgsConstructor
@Builder
@Data
@Table(name = "doctor")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "pesel")
    private String pesel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    private Set<FreeTermEntity> freeTerms;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    private Set<VisitEntity> visits;

}
