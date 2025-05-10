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
    private Integer userId;

    @Column
    private Integer courseId;

    @Column
    private Double amount;

    @Column
    private String status;

    @Column
    private String description;

    @Column
    private String issueDate;

}
