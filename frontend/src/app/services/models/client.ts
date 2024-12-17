/* tslint:disable */
/* eslint-disable */
import { Reservation } from '../models/reservation';
import { Role } from '../models/role';
export interface Client {
  dayOfBirth: string;
  deleted?: boolean;
  email?: string;
  enabled?: boolean;
  firstname: string;
  id?: number;
  lastname: string;
  password?: string;
  phoneNumber?: string;
  reservations?: Array<Reservation>;
  roles?: Array<Role>;
}
