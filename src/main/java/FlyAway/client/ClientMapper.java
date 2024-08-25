package FlyAway.client;

import FlyAway.client.dto.ClientDto;
import FlyAway.client.dto.ClientRegistrationDto;
import FlyAway.client.dto.ClientReservationDto;
import FlyAway.reservation.ReservationMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ReservationMapper.class)
public interface ClientMapper {
    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    ClientDto clientToClientDto(Client client);

    Client clientRegistrationDtoToClient(ClientRegistrationDto clientRegistrationDto);

    ClientReservationDto clientToClientReservationDto(Client client);

}
