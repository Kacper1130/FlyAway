/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { addAircraft } from '../fn/aircraft/add-aircraft';
import { AddAircraft$Params } from '../fn/aircraft/add-aircraft';
import { Aircraft } from '../models/aircraft';
import { getAllAircraft } from '../fn/aircraft/get-all-aircraft';
import { GetAllAircraft$Params } from '../fn/aircraft/get-all-aircraft';

@Injectable({ providedIn: 'root' })
export class AircraftService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `addAircraft()` */
  static readonly AddAircraftPath = '/api/v1/aircraft/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addAircraft()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addAircraft$Response(params: AddAircraft$Params, context?: HttpContext): Observable<StrictHttpResponse<Aircraft>> {
    return addAircraft(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addAircraft$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addAircraft(params: AddAircraft$Params, context?: HttpContext): Observable<Aircraft> {
    return this.addAircraft$Response(params, context).pipe(
      map((r: StrictHttpResponse<Aircraft>): Aircraft => r.body)
    );
  }

  /** Path part for operation `getAllAircraft()` */
  static readonly GetAllAircraftPath = '/api/v1/aircraft';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllAircraft()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllAircraft$Response(params?: GetAllAircraft$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Aircraft>>> {
    return getAllAircraft(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllAircraft$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllAircraft(params?: GetAllAircraft$Params, context?: HttpContext): Observable<Array<Aircraft>> {
    return this.getAllAircraft$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Aircraft>>): Array<Aircraft> => r.body)
    );
  }

}
