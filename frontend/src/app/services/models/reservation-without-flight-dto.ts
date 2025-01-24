/* tslint:disable */
/* eslint-disable */
import { ClientDto } from '../models/client-dto';
export interface ReservationWithoutFlightDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  cancelled?: boolean;
  clientDto?: ClientDto;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
}
