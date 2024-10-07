/* tslint:disable */
/* eslint-disable */
import { Airport } from '../models/airport';
export interface Country {
  airports: Array<Airport>;
  enabled?: boolean;
  id?: number;
  name: string;
}
