package edu.bbte.idde.keim2152.spring.service;

import java.util.Collection;
import java.util.List;

import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserCarlistingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserReducedDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDetailedDto create(UserIncomingDto userIncomingDto);
    
    UserDetailedDto update(Long id, UserIncomingDto userIncomingDto);

    void delete(Long id);

    UserDetailedDto findById(Long id);

    Collection<UserReducedDto> findAll();

    Collection<UserReducedDto> findByEmail(String name);

    UserDetailedDto createCarListing(Long userId, UserCarlistingIncomingDto userCarlistingIncomingDto, List<MultipartFile> images);

    UserDetailedDto deleteCarListing(Long userId, Long carListingId);

    Collection<UserReducedDto> findAllPaginated(Integer page, Integer number, String attribute, Boolean isAsc);

    Collection<CarListingReducedDto> findCarsPaginatedByUser(Long userId,
                                                             Integer page,
                                                             Integer number,
                                                             String attribute,
                                                             Boolean isAsc);

    UserDetailedDto updateCarListing(Long userId, Long carListingId, UserCarlistingIncomingDto dto);

    UserDetailedDto updateLang(Long id, String lang);

    UserDetailedDto updateTheme(Long id, String theme);
}
