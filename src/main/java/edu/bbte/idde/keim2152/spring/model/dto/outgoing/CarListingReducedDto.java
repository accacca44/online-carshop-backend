package edu.bbte.idde.keim2152.spring.model.dto.outgoing;

import lombok.Data;

@Data
public class CarListingReducedDto {
    Long id;
    String make;
    String model;
    Float price;

}
