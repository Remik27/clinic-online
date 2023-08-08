package pl.zajavka.infrastructure.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.time.OffsetDateTime;

@Entity
@AllArgsConstructor
@With
@Builder
@Data
@Table(name = "visit")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description")
    private String description;

    @Column(name = "term")
    private OffsetDateTime term;

    @Column(name = "status")
    private String status;

    @Column(name = "disease")
    private String disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private DoctorEntity doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

}
