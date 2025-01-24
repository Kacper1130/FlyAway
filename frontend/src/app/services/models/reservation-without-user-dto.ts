/* tslint:disable */
/* eslint-disable */
import { FlightDto } from '../models/flight-dto';
export interface ReservationWithoutUserDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  cancelled?: boolean;
  flightDto?: FlightDto;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
}
