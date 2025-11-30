package FlyAway.client;

import FlyAway.client.dto.ClientDto;
import FlyAway.client.dto.ClientNameDto;
import FlyAway.client.dto.ClientRegistrationDto;
import FlyAway.client.dto.ClientReservationDto;
import FlyAway.reservation.ReservationMapper;
import FlyAway.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ReservationMapper.class)
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto clientToClientDto(User client);

    User clientRegistrationDtoToClient(ClientRegistrationDto clientRegistrationDto);

    ClientReservationDto clientToClientReservationDto(User client);

    ClientNameDto clientToClientNameDto(User client);
}
