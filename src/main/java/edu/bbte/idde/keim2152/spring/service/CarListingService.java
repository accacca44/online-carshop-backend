package edu.bbte.idde.keim2152.spring.service;

import edu.bbte.idde.keim2152.spring.model.dto.incoming.CarListingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;

import java.util.Collection;

public interface CarListingService {

    CarListingDetailedDto create(CarListingIncomingDto carListingIncomingDto);
    
    CarListingDetailedDto update(Long id, CarListingIncomingDto carListingIncomingDto);

    void delete(Long id);

    CarListingDetailedDto findById(Long id);

    Collection<CarListingReducedDto> findAll();

    Collection<CarListingReducedDto> findByMake(String make);

    Collection<CarListingReducedDto> findAllPaginatedByUserId(Long userId,
                                                              Integer page,
                                                              Integer number,
                                                              String attribute,
                                                              Boolean isAsc);

    Collection<CarListingReducedDto> findAllPaginated(Integer page,
                                                      Integer number,
                                                      String attribute,
                                                      Boolean isAsc);
}
