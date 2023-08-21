package pl.zajavka.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@AllArgsConstructor
@EqualsAndHashCode(of = "pesel")
@NoArgsConstructor
@Builder
@With
@Data
@Table(name = "patient")
public class PatientEntity {

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

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "patient")
    private Set<VisitEntity> visits;


}

