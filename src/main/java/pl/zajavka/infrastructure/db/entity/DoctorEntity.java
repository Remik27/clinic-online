package pl.zajavka.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@With
@Builder
@Data
@Table(name = "doctor")
public class DoctorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "clinic_user_id")
    private Integer clinicUserId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "pesel")
    private String pesel;

    @Column(name = "specialization")
    private String specialization;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    private Set<FreeTermEntity> freeTerms;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "doctor")
    private Set<VisitEntity> visits;

}
