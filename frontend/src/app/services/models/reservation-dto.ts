/* tslint:disable */
/* eslint-disable */
import { ClientDto } from '../models/client-dto';
import { FlightDto } from '../models/flight-dto';
export interface ReservationDto {
  cancelled?: boolean;
  clientDto?: ClientDto;
  flightDto?: FlightDto;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
}
