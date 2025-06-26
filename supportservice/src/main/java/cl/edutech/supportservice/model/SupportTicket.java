package cl.edutech.supportservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "support_tickets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SupportTicket {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String userRut;

    @Column(nullable = false)
    private String courseId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String description;

}
