package edu.bbte.idde.keim2152.spring.controller;

import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserCarlistingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserDetailedDto;
import edu.bbte.idde.keim2152.spring.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/carlistings")
public class UserCarListingController {
    private final UserService userService;

    /* Commented out because of paginated GET req */
    //    @GetMapping("/")
    //    public Collection<CarListingReducedDto> findAllByUserId(@PathVariable Long userId) {
    //        log.info("GET /api/users/" + userId + "/carlistings");
    //        var user = userService.findById(userId);
    //        var carListings = user.getCarListings();
    //        log.info("Response: " + carListings);
    //        return carListings;
    //    }

    @GetMapping
    public Collection<CarListingReducedDto> findAllPaginated(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "0") @Positive Integer page,
            @RequestParam(name = "number", defaultValue = "5") @Positive Integer number,
            @RequestParam(name = "attribute", defaultValue = "id") String attribute,
            @RequestParam(name = "asc", defaultValue = "false") Boolean isAsc) {
        return userService.findCarsPaginatedByUser(userId, page, number, attribute, isAsc);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE})
    public UserDetailedDto createCarListingByUserId(@PathVariable Long userId,
                                                    @RequestPart("images") List<MultipartFile> images,
                                                    @ModelAttribute @Valid UserCarlistingIncomingDto userCarlistingIncomingDto) {
        return userService.createCarListing(userId, userCarlistingIncomingDto, images);

    }

    @PutMapping("{carListingId}")
    public UserDetailedDto updateCarByUserId(@PathVariable Long userId, @PathVariable Long carListingId,
        @RequestBody @Valid UserCarlistingIncomingDto dto) {
        return userService.updateCarListing(userId, carListingId, dto);
    }

    @DeleteMapping("/{carListingId}")
    public UserDetailedDto deleteCarListingByUserId(@PathVariable Long userId, @PathVariable Long carListingId) {
        log.info("DELETE /api/users/" + userId + "/carlistings/" + carListingId);
        var user = userService.deleteCarListing(userId, carListingId);
        log.info("Response: " + user);
        return user;
    }
}
