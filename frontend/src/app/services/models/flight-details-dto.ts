/* tslint:disable */
/* eslint-disable */
import { Aircraft } from '../models/aircraft';
import { AirportDto } from '../models/airport-dto';
import { ReservationWithoutFlightDto } from '../models/reservation-without-flight-dto';
export interface FlightDetailsDto {
  aircraft: Aircraft;
  arrivalAirportDto: AirportDto;
  arrivalDate: string;
  cabinClassPrices: {
[key: string]: number;
};
  departureAirportDto: AirportDto;
  departureDate: string;
  id: string;
  reservations?: Array<ReservationWithoutFlightDto>;
}
