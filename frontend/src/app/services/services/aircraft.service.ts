/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { Aircraft } from '../models/aircraft';
import { createAircraft } from '../fn/aircraft/create-aircraft';
import { CreateAircraft$Params } from '../fn/aircraft/create-aircraft';
import { getAllAircraft } from '../fn/aircraft/get-all-aircraft';
import { GetAllAircraft$Params } from '../fn/aircraft/get-all-aircraft';

@Injectable({ providedIn: 'root' })
export class AircraftService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
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

  /** Path part for operation `createAircraft()` */
  static readonly CreateAircraftPath = '/api/v1/aircraft';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createAircraft()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createAircraft$Response(params: CreateAircraft$Params, context?: HttpContext): Observable<StrictHttpResponse<Aircraft>> {
    return createAircraft(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createAircraft$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createAircraft(params: CreateAircraft$Params, context?: HttpContext): Observable<Aircraft> {
    return this.createAircraft$Response(params, context).pipe(
      map((r: StrictHttpResponse<Aircraft>): Aircraft => r.body)
    );
  }

}
