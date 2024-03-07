package edu.bbte.idde.keim2152.spring.auth.payload;

import lombok.Data;

@Data
public class IntrospectResponse {
    private String username;
    private Long userId;
    private String role;
    private String lang;
    private String theme;

    public IntrospectResponse(String username, Long userId, String role, String lang, String theme) {
        this.username = username;
        this.userId = userId;
        this.role = role;
        this.lang = lang;
        this.theme = theme;
    }
}
