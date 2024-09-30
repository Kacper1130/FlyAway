/* tslint:disable */
/* eslint-disable */
import { Country } from '../models/country';
export interface Airport {
  IATACode: string;
  city: string;
  country?: Country;
  id?: string;
  name: string;
}
