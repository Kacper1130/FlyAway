/* tslint:disable */
/* eslint-disable */
import { FlightBasicDto } from '../models/flight-basic-dto';
export interface ReservationSummaryClientDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  flightDto?: FlightBasicDto;
  id?: string;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
