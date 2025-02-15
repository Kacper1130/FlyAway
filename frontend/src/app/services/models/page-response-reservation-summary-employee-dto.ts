/* tslint:disable */
/* eslint-disable */
import { ReservationSummaryEmployeeDto } from '../models/reservation-summary-employee-dto';
export interface PageResponseReservationSummaryEmployeeDto {
  content?: Array<ReservationSummaryEmployeeDto>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
