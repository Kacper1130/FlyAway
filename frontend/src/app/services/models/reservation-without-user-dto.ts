/* tslint:disable */
/* eslint-disable */
import { FlightDto } from '../models/flight-dto';
export interface ReservationWithoutUserDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  flightDto?: FlightDto;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
