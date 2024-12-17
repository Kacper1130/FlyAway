/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { add } from '../fn/flight/add';
import { Add$Params } from '../fn/flight/add';
import { FlightDetailsDto } from '../models/flight-details-dto';
import { FlightDto } from '../models/flight-dto';
import { getAll } from '../fn/flight/get-all';
import { GetAll$Params } from '../fn/flight/get-all';
import { getAllFullFlights } from '../fn/flight/get-all-full-flights';
import { GetAllFullFlights$Params } from '../fn/flight/get-all-full-flights';
import { getFlightDetails } from '../fn/flight/get-flight-details';
import { GetFlightDetails$Params } from '../fn/flight/get-flight-details';

@Injectable({ providedIn: 'root' })
export class FlightService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `add()` */
  static readonly AddPath = '/api/v1/flights/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `add()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  add$Response(params: Add$Params, context?: HttpContext): Observable<StrictHttpResponse<FlightDto>> {
    return add(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `add$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  add(params: Add$Params, context?: HttpContext): Observable<FlightDto> {
    return this.add$Response(params, context).pipe(
      map((r: StrictHttpResponse<FlightDto>): FlightDto => r.body)
    );
  }

  /** Path part for operation `getAll()` */
  static readonly GetAllPath = '/api/v1/flights';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll$Response(params?: GetAll$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<FlightDto>>> {
    return getAll(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAll$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll(params?: GetAll$Params, context?: HttpContext): Observable<Array<FlightDto>> {
    return this.getAll$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<FlightDto>>): Array<FlightDto> => r.body)
    );
  }

  /** Path part for operation `getAllFullFlights()` */
  static readonly GetAllFullFlightsPath = '/api/v1/flights/full';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllFullFlights()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllFullFlights$Response(params?: GetAllFullFlights$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<FlightDetailsDto>>> {
    return getAllFullFlights(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllFullFlights$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllFullFlights(params?: GetAllFullFlights$Params, context?: HttpContext): Observable<Array<FlightDetailsDto>> {
    return this.getAllFullFlights$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<FlightDetailsDto>>): Array<FlightDetailsDto> => r.body)
    );
  }

  /** Path part for operation `getFlightDetails()` */
  static readonly GetFlightDetailsPath = '/api/v1/flights/full/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getFlightDetails()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFlightDetails$Response(params: GetFlightDetails$Params, context?: HttpContext): Observable<StrictHttpResponse<FlightDetailsDto>> {
    return getFlightDetails(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getFlightDetails$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFlightDetails(params: GetFlightDetails$Params, context?: HttpContext): Observable<FlightDetailsDto> {
    return this.getFlightDetails$Response(params, context).pipe(
      map((r: StrictHttpResponse<FlightDetailsDto>): FlightDetailsDto => r.body)
    );
  }

}
