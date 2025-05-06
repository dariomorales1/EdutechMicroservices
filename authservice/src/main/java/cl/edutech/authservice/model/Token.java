package cl.edutech.authservice.model;

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
@Table(name="TOKENS")
public class Token {

    @Id
    private String token;

    @Column
    private String username;

    @Column
    private String password;


}
