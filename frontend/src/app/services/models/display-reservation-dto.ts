/* tslint:disable */
/* eslint-disable */
export interface DisplayReservationDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  cancelled?: boolean;
  clientId?: number;
  flightId?: string;
  id?: string;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
}
