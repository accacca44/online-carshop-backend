package edu.bbte.idde.keim2152.spring.auth.payload;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private String role;
    private String lang;
    private String theme;

    public JwtResponse(String token, Long id, String username, String email, String role, String lang, String theme) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.lang = lang;
        this.theme = theme;
    }
}
