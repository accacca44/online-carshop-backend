package edu.bbte.idde.keim2152.spring.model.dto.incoming;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserIncomingDto {
    @NotEmpty
    private String fullName;
    
    @NotEmpty
    private String email; 
}
