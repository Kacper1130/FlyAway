/* tslint:disable */
/* eslint-disable */
import { ReservationDto } from '../models/reservation-dto';
export interface ReservationPaymentResponseDto {
  paymentUrl?: string;
  reservation?: ReservationDto;
}
