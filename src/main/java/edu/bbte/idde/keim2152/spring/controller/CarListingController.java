package edu.bbte.idde.keim2152.spring.controller;

import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;
import edu.bbte.idde.keim2152.spring.service.CarListingService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collection;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/carlistings")
public class CarListingController {
    private final CarListingService carListingService;

    @GetMapping
    public Collection<CarListingReducedDto> findAllPaginated(
        @RequestParam(name = "page", defaultValue = "0") @Positive Integer page,
        @RequestParam(name = "number", defaultValue = "100") @Positive Integer number,
        @RequestParam(name = "attribute", defaultValue = "id") String attribute,
        @RequestParam(name = "asc", defaultValue = "false") Boolean isAsc) {
        return carListingService.findAllPaginated(page, number, attribute, isAsc);
    }

    @GetMapping("/{id}")
    public CarListingDetailedDto findCarListing(@PathVariable Long id) {
        var car = carListingService.findById(id);
        var imageBytes = car.getImages().get(0).getContent();
        log.debug(Arrays.toString(imageBytes));
        return car;
    }



}
