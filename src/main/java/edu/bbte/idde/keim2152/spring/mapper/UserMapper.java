package edu.bbte.idde.keim2152.spring.mapper;

import edu.bbte.idde.keim2152.spring.auth.payload.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;

import edu.bbte.idde.keim2152.spring.model.domain.User;
import edu.bbte.idde.keim2152.spring.model.dto.incoming.UserIncomingDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserDetailedDto;
import edu.bbte.idde.keim2152.spring.model.dto.outgoing.UserReducedDto;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = {User.class})
public interface UserMapper {
    @Mapping(target = "carListings", ignore = true)
    @Mapping(target = "id", ignore = true)
    void userIncomingToDomain(UserIncomingDto userDto, @MappingTarget User user);

    UserDetailedDto domainToDetailedOutgoing(User user);

    UserReducedDto domainToReducedOutgoing(User user);

    Collection<UserReducedDto> domainsToReducedOutgoings(Collection<User> users);

    @Mapping(target = "carListings", ignore = true)
    @Mapping(target = "id", ignore = true)
    void registerToDomain(RegisterRequest registerRequest, @MappingTarget User user);
}
