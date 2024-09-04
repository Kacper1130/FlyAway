/* tslint:disable */
/* eslint-disable */
import { ReservationWithoutUserDto } from '../models/reservation-without-user-dto';
export interface ClientReservationDto {
  dayOfBirth?: string;
  email?: string;
  firstname?: string;
  lastname?: string;
  phoneNumber?: string;
  reservations?: Array<ReservationWithoutUserDto>;
}
