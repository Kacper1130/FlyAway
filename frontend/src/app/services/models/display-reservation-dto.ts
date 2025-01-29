/* tslint:disable */
/* eslint-disable */
export interface DisplayReservationDto {
  cabinClass?: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  clientId?: number;
  flightId?: string;
  id?: string;
  price?: number;
  reservationDate?: string;
  seatNumber?: number;
  status?: 'PENDING' | 'ACTIVE' | 'CANCELLED' | 'EXPIRED' | 'COMPLETED' | 'FAILED';
}
