package cl.edutech.authservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String rut;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
