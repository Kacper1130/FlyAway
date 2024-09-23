/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { activateEmployee } from '../fn/employee/activate-employee';
import { ActivateEmployee$Params } from '../fn/employee/activate-employee';

@Injectable({ providedIn: 'root' })
export class EmployeeService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `activateEmployee()` */
  static readonly ActivateEmployeePath = '/api/v1/employee/activate';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `activateEmployee()` instead.
   *
   * This method doesn't expect any request body.
   */
  activateEmployee$Response(params?: ActivateEmployee$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return activateEmployee(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `activateEmployee$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  activateEmployee(params?: ActivateEmployee$Params, context?: HttpContext): Observable<{
}> {
    return this.activateEmployee$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

}
