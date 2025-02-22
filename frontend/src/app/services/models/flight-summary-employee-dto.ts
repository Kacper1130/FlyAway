/* tslint:disable */
/* eslint-disable */
import { AirportCityIataCodeDto } from '../models/airport-city-iata-code-dto';
export interface FlightSummaryEmployeeDto {
  arrivalAirportDto?: AirportCityIataCodeDto;
  arrivalDate?: string;
  cabinClassPrices?: {
[key: string]: number;
};
  departureAirportDto?: AirportCityIataCodeDto;
  departureDate?: string;
  id?: string;
}
