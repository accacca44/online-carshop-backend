package edu.bbte.idde.keim2152.spring.controller;

import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.ResponseMessageDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserReducedDto;
import edu.bbte.idde.keim2152.spring.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Collection<UserReducedDto> findAllPaginated(
            @RequestParam(name = "page", defaultValue = "0") @Positive Integer page,
            @RequestParam(name = "number", defaultValue = "5") @Positive Integer number,
            @RequestParam(name = "attribute", defaultValue = "id") String attribute,
            @RequestParam(name = "asc", defaultValue = "false") Boolean isAsc) {
        return userService.findAllPaginated(page, number, attribute, isAsc);
    }

    @GetMapping("/{id}")
    public UserDetailedDto findById(@PathVariable Long id) {
        log.info("GET /api/users/" + id);
        UserDetailedDto userDetailedDto = userService.findById(id);

        log.info("Response: " + userDetailedDto);
        return userDetailedDto;
    }

    /* Commented out bc of pagination */
    //    @GetMapping
    //    public Collection<UserReducedDto> findAll(@RequestParam(required = false) String email) {
    //        if (null != email) {
    //            return findAllByEmail(email);
    //        }
    //
    //        log.info("GET /api/users");
    //        Collection<UserReducedDto> userReducedDtos = userService.findAll();
    //
    //        log.info("Respoonse: " + userReducedDtos);
    //        return userReducedDtos;
    //    }

    @PostMapping
    public UserDetailedDto create(@RequestBody @Valid UserIncomingDto userIncomingDto) {
        log.info("POST /api/users");
        UserDetailedDto userDetailedDto = userService.create(userIncomingDto);

        log.info("Response: " + userDetailedDto);
        return userDetailedDto;
    }

    @PutMapping("/{id}")
    public UserDetailedDto update(@PathVariable Long id, @RequestBody @Valid UserIncomingDto userIncomingDto) {
        log.info("PUT /api/users/" + id);
        UserDetailedDto userDetailedDto = userService.update(id, userIncomingDto);

        log.info("Response: " + userDetailedDto);
        return userDetailedDto;
    }

    @PutMapping("/{id}/lang")
    public UserDetailedDto updateLand(@PathVariable Long id,
                                      @RequestParam String lang) {
        UserDetailedDto userDetailedDto = userService.updateLang(id, lang);
        return userDetailedDto;
    }

    @PutMapping("/{id}/theme")
    public UserDetailedDto updateTheme(@PathVariable Long id,
                                      @RequestParam String theme) {
        log.info("New theme" + theme);
        UserDetailedDto userDetailedDto = userService.updateTheme(id, theme);
        return userDetailedDto;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseMessageDto delete(@PathVariable Long id) {
        log.info("DELETE /api/users/" + id);
        userService.delete(id);

        ResponseMessageDto responseMessageDto = new ResponseMessageDto("User deleted successfully");
        log.info("Response: " + responseMessageDto);
        return responseMessageDto;
    }
}
