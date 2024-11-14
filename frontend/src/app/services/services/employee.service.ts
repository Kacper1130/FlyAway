/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addEmployee } from '../fn/employee/add-employee';
import { AddEmployee$Params } from '../fn/employee/add-employee';
import { DisplayEmployeeDto } from '../models/display-employee-dto';
import { EmployeeCredentialsDto } from '../models/employee-credentials-dto';
import { getAllEmployees } from '../fn/employee/get-all-employees';
import { GetAllEmployees$Params } from '../fn/employee/get-all-employees';

@Injectable({ providedIn: 'root' })
export class EmployeeService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addEmployee()` */
  static readonly AddEmployeePath = '/api/v1/employees/add';

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
  static readonly GetAllEmployeesPath = '/api/v1/employees';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllEmployees()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEmployees$Response(params?: GetAllEmployees$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DisplayEmployeeDto>>> {
    return getAllEmployees(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllEmployees$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEmployees(params?: GetAllEmployees$Params, context?: HttpContext): Observable<Array<DisplayEmployeeDto>> {
    return this.getAllEmployees$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<DisplayEmployeeDto>>): Array<DisplayEmployeeDto> => r.body)
    );
  }

}
