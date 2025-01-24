/* tslint:disable */
/* eslint-disable */
import { Aircraft } from '../models/aircraft';
import { AirportDto } from '../models/airport-dto';
export interface CreateFlightDto {
  aircraft: Aircraft;
  arrivalAirportDto: AirportDto;
  arrivalDate: string;
  cabinClassPrices: {
[key: string]: number;
};
  departureAirportDto: AirportDto;
  departureDate: string;
}
