package edu.bbte.idde.keim2152.spring.model.dto.outgoing;

import lombok.Data;

import java.util.List;

@Data
public class CarListingDetailedDto {
    private Long id;
    private String make;
    private String model;
    private Float price;
    private Integer mileage;
    private Integer yearOfManufacture;
    private String phoneNumber;
    private Long userId;
    private List<ReducedImageDto> images;
    private Boolean heatedSeats;
    private Boolean automaticGearBox;
    private Boolean bluetooth;
    private Boolean sunroof;
}
