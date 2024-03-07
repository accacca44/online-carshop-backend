package edu.bbte.idde.keim2152.spring.model.dto.outgoing;

import lombok.Data;

@Data
public class UserCarListingDto {
    Long id;
    String make;
    String model;
    Float price;
}
