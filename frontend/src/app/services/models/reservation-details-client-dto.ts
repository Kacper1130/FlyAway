/* tslint:disable */
/* eslint-disable */
import { FlightDetailsClientDto } from '../models/flight-details-client-dto';
export interface ReservationDetailsClientDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  flightDto?: FlightDetailsClientDto;
  id?: string;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
