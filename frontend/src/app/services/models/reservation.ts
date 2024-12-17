/* tslint:disable */
/* eslint-disable */
import { Client } from '../models/client';
import { Flight } from '../models/flight';
export interface Reservation {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  cancelled?: boolean;
  client?: Client;
  flight?: Flight;
  id?: string;
  price: number;
  reservationDate: string;
  seatNumber: number;
}
