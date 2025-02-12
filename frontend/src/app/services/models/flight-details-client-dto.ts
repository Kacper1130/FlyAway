/* tslint:disable */
/* eslint-disable */
import { AircraftDto } from '../models/aircraft-dto';
import { AirportBasicDto } from '../models/airport-basic-dto';
export interface FlightDetailsClientDto {
  aircraftDto?: AircraftDto;
  arrivalAirportDto?: AirportBasicDto;
  arrivalDate?: string;
  departureAirportDto?: AirportBasicDto;
  departureDate?: string;
}
