package cl.edutech.authservice.dto;

public class AuthUserDTO {
    private String email;
    private String token;

    // Constructores
    public AuthUserDTO() { }
    public AuthUserDTO(String email, String token) {
        this.email = email;
        this.token = token;
    }

    // Getters y setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

