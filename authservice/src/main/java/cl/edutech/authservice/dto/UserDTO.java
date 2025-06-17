package cl.edutech.authservice.dto;

public class UserDTO {
    private String email;
    private String password; // Este es el hash bcrypt que viene de UserService
    private String role;     // Opcional, según tu modelo

    // Constructores
    public UserDTO() { }
    public UserDTO(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    // Getters y setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

