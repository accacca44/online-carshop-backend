package edu.bbte.idde.keim2152.spring.auth.payload;

import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserIncomingDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterRequest extends UserIncomingDto {
    @NotBlank
    private String role;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
