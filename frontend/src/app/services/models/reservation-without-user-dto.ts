/* tslint:disable */
/* eslint-disable */
import { FlightDto } from '../models/flight-dto';
export interface ReservationWithoutUserDto {
  cancelled?: boolean;
  flightDto?: FlightDto;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
}
