package cl.edutech.supportservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "supports")
public class SupportTicket {

    @Id
    private Integer id;

    @Column
    private String userRut;

    @Column
    private String courseId;

    @Column
    private String description;

    @Column
    private String status;
}
