/* tslint:disable */
/* eslint-disable */
import { ClientDto } from '../models/client-dto';
export interface ReservationWithoutFlightDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  clientDto?: ClientDto;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
