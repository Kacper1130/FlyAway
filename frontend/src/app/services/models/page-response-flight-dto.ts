/* tslint:disable */
/* eslint-disable */
import { FlightDto } from '../models/flight-dto';
export interface PageResponseFlightDto {
  content?: Array<FlightDto>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
