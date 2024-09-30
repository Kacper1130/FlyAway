/* tslint:disable */
/* eslint-disable */
import { SeatClassRange } from '../models/seat-class-range';
export interface Aircraft {
  id?: string;
  model: string;
  productionYear: number;
  registration: string;
  seatClassRanges: {
[key: string]: SeatClassRange;
};
  totalSeats: number;
}
