/* tslint:disable */
/* eslint-disable */
import { Role } from '../models/role';
export interface Employee {
  email?: string;
  enabled?: boolean;
  firstname: string;
  hireDate?: string;
  id?: number;
  lastname: string;
  mustChangePassword?: boolean;
  password?: string;
  phoneNumber?: string;
  roles?: Array<Role>;
}
