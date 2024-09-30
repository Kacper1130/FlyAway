/* tslint:disable */
/* eslint-disable */
import { Aircraft } from '../models/aircraft';
import { Airport } from '../models/airport';
export interface FlightDto {
  aircraft: Aircraft;
  arrivalAirport: Airport;
  arrivalDate: string;
  cabinClassPrices: {
[key: string]: number;
};
  departureAirport: Airport;
  departureDate: string;
}
