/* tslint:disable */
/* eslint-disable */
import { ClientNameDto } from '../models/client-name-dto';
import { FlightBasicDto } from '../models/flight-basic-dto';
export interface ReservationSummaryEmployeeDto {
  clientDto?: ClientNameDto;
  flightDto?: FlightBasicDto;
  id?: string;
  reservationDate?: string;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
