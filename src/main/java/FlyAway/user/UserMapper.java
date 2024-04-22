package FlyAway.user;

import FlyAway.reservation.ReservationMapper;
import FlyAway.user.dto.UserDto;
import FlyAway.user.dto.UserRegistrationDto;
import FlyAway.user.dto.UserReservationDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = ReservationMapper.class)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

    UserReservationDto userToUserReservationDto(User user);

}
