package edu.bbte.idde.keim2152.spring.model.dto.incoming;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CarListingIncomingDto {
    @NotEmpty
    @Size(min = 1, max = 32)
    private String make;
    
    @NotEmpty
    @Size(min = 1, max = 32)
    private String model;

    @NotNull
    @Positive
    private Float price;
    
    @NotNull
    @Positive
    private Integer mileage;
    
    @NotNull
    private Integer yearOfManufacture;
    
    @NotEmpty
    private String phoneNumber;

    @NotNull
    private Long userId;
    @NotNull
    private Boolean heatedSeats;
    @NotNull
    private Boolean automaticGearBox;
    @NotNull
    private Boolean bluetooth;
    @NotNull
    private Boolean sunroof;
}
