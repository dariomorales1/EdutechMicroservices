package cl.edutech.billingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="billings")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class Billing {

    @Id
    private Integer billingId;

    @Column
    private String userId;

    @Column
    private String courseId;

    @Column
    private Integer enrollmentId;

}
