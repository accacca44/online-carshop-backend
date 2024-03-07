package edu.bbte.idde.keim2152.spring.model.dto.outgoing;

import java.util.List;

import lombok.Data;

@Data
public class UserDetailedDto {
    private Long id;
    private String fullName;    
    private String email; 
    private List<CarListingReducedDto> carListings;
    private String username;
    private String role;
}
