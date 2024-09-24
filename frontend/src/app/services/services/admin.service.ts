/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addEmployee } from '../fn/admin/add-employee';
import { AddEmployee$Params } from '../fn/admin/add-employee';
import { Employee } from '../models/employee';
import { EmployeeCredentialsDto } from '../models/employee-credentials-dto';
import { getAllEmployees } from '../fn/admin/get-all-employees';
import { GetAllEmployees$Params } from '../fn/admin/get-all-employees';

@Injectable({ providedIn: 'root' })
export class AdminService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addEmployee()` */
  static readonly AddEmployeePath = '/api/v1/admin/employees/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addEmployee()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addEmployee$Response(params: AddEmployee$Params, context?: HttpContext): Observable<StrictHttpResponse<EmployeeCredentialsDto>> {
    return addEmployee(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addEmployee$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addEmployee(params: AddEmployee$Params, context?: HttpContext): Observable<EmployeeCredentialsDto> {
    return this.addEmployee$Response(params, context).pipe(
      map((r: StrictHttpResponse<EmployeeCredentialsDto>): EmployeeCredentialsDto => r.body)
    );
  }

  /** Path part for operation `getAllEmployees()` */
  static readonly GetAllEmployeesPath = '/api/v1/admin/employees';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllEmployees()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEmployees$Response(params?: GetAllEmployees$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Employee>>> {
    return getAllEmployees(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllEmployees$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEmployees(params?: GetAllEmployees$Params, context?: HttpContext): Observable<Array<Employee>> {
    return this.getAllEmployees$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Employee>>): Array<Employee> => r.body)
    );
  }

}
