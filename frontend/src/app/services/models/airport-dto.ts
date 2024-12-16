/* tslint:disable */
/* eslint-disable */
import { CountryDto } from '../models/country-dto';
export interface AirportDto {
  IATACode: string;
  city: string;
  country: CountryDto;
  enabled?: boolean;
  id: string;
  name: string;
}
