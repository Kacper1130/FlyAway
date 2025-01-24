/* tslint:disable */
/* eslint-disable */
export interface CreateReservationDto {
  cabinClass: 'FIRST' | 'BUSINESS' | 'ECONOMY';
  flightId: string;
  seatNumber: number;
}
