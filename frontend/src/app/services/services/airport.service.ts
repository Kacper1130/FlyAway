/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { add2 } from '../fn/airport/add-2';
import { Add2$Params } from '../fn/airport/add-2';
import { Airport } from '../models/airport';
import { getAll2 } from '../fn/airport/get-all-2';
import { GetAll2$Params } from '../fn/airport/get-all-2';

@Injectable({ providedIn: 'root' })
export class AirportService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `add2()` */
  static readonly Add2Path = '/api/v1/airports/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `add2()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  add2$Response(params: Add2$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return add2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `add2$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  add2(params: Add2$Params, context?: HttpContext): Observable<{
}> {
    return this.add2$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `getAll2()` */
  static readonly GetAll2Path = '/api/v1/airports';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll2()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll2$Response(params?: GetAll2$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Airport>>> {
    return getAll2(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAll2$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll2(params?: GetAll2$Params, context?: HttpContext): Observable<Array<Airport>> {
    return this.getAll2$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Airport>>): Array<Airport> => r.body)
    );
  }

}
