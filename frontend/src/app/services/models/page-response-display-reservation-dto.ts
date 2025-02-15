/* tslint:disable */
/* eslint-disable */
import { DisplayReservationDto } from '../models/display-reservation-dto';
export interface PageResponseDisplayReservationDto {
  content?: Array<DisplayReservationDto>;
  first?: boolean;
  last?: boolean;
  number?: number;
  size?: number;
  totalElements?: number;
  totalPages?: number;
}
