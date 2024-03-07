package edu.bbte.idde.keim2152.spring.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "car_images")
@NoArgsConstructor
@AllArgsConstructor
public class CarImage extends BaseEntity {
    private String path;

    @ManyToOne
    @JsonIgnore
    private CarListing carListing;
}
