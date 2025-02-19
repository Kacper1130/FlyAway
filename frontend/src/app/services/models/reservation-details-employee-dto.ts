/* tslint:disable */
/* eslint-disable */
import { ClientDto } from '../models/client-dto';
import { FlightDetailsEmployeeDto } from '../models/flight-details-employee-dto';
export interface ReservationDetailsEmployeeDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  clientDto?: ClientDto;
  flightDto?: FlightDetailsEmployeeDto;
  id?: string;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
