package edu.bbte.idde.keim2152.spring.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

import edu.bbte.idde.keim2152.spring.model.domain.CarListing;
import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.CarListingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserCarlistingIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.CarListingReducedDto;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = {User.class})
public interface CarListingMapper {

    @Mapping(target = "user", expression = "java(new User(carListingDto.getUserId()))")
    CarListing carListingIncomingToDomain(CarListingIncomingDto carListingDto);

    @Mapping(target = "userId", expression = "java(carListing.getUser().getId())")
    @Mapping(target = "images", ignore = true)
    CarListingDetailedDto domainToDetailedOutgoing(CarListing carListing);

    CarListingReducedDto domainToReducedOutgoing(CarListing carListing);

    CarListing userCarlistingToDomain(UserCarlistingIncomingDto userCarlistingDto);

    Collection<CarListingReducedDto> domainsToReducedOutgoings(Collection<CarListing> carListings);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void userCarsToDomain(UserCarlistingIncomingDto userCarDto, @MappingTarget CarListing carListing);
    
}
