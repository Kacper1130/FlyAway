/* tslint:disable */
/* eslint-disable */
import { AirportBasicDto } from '../models/airport-basic-dto';
export interface FlightDetailsEmployeeDto {
  arrivalAirportDto?: AirportBasicDto;
  arrivalDate?: string;
  departureAirportDto?: AirportBasicDto;
  departureDate?: string;
  id?: string;
}
