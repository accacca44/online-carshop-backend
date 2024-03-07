package edu.bbte.idde.keim2152.spring.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.swing.text.StyledEditorKit;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "carlistings")
@NoArgsConstructor
@AllArgsConstructor
public class CarListing extends BaseEntity {
    private String make;
    private String model;
    private Float price;
    private Integer mileage;
    private Integer yearOfManufacture;
    private String phoneNumber;
    private Boolean heatedSeats;
    private Boolean automaticGearBox;
    private Boolean bluetooth;
    private Boolean sunroof;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    @OneToMany(mappedBy = "carListing", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CarImage> images;

    @Override
    public String toString() {
        return "CarListing{"
                + "id=" + id + '\''
                + "make='" + make + '\''
                + ", model='" + model + '\''
                + ", price=" + price
                + ", mileage=" + mileage
                + ", yearOfManufacture="
                + yearOfManufacture + ", phoneNumber='" + phoneNumber + '\''
                + ", userId="
                + user.getId();
    }
}