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
import { AvailableSeatsDto } from '../models/available-seats-dto';
import { FlightDetailsDto } from '../models/flight-details-dto';
import { FlightDto } from '../models/flight-dto';
import { FlightSummaryEmployeeDto } from '../models/flight-summary-employee-dto';
import { getAllFullFlights } from '../fn/flight/get-all-full-flights';
import { GetAllFullFlights$Params } from '../fn/flight/get-all-full-flights';
import { getAvailableSeats } from '../fn/flight/get-available-seats';
import { GetAvailableSeats$Params } from '../fn/flight/get-available-seats';
import { getFlightDetails } from '../fn/flight/get-flight-details';
import { GetFlightDetails$Params } from '../fn/flight/get-flight-details';
import { getFlights } from '../fn/flight/get-flights';
import { GetFlights$Params } from '../fn/flight/get-flights';
import { getFlightsByFilter } from '../fn/flight/get-flights-by-filter';
import { GetFlightsByFilter$Params } from '../fn/flight/get-flights-by-filter';
import { PageResponseFlightDto } from '../models/page-response-flight-dto';

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

  /** Path part for operation `getFlights()` */
  static readonly GetFlightsPath = '/api/v1/flights';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getFlights()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFlights$Response(params?: GetFlights$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFlightDto>> {
    return getFlights(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getFlights$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFlights(params?: GetFlights$Params, context?: HttpContext): Observable<PageResponseFlightDto> {
    return this.getFlights$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseFlightDto>): PageResponseFlightDto => r.body)
    );
  }

  /** Path part for operation `getAvailableSeats()` */
  static readonly GetAvailableSeatsPath = '/api/v1/flights/{id}/available-seats/{cabin-class}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAvailableSeats()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAvailableSeats$Response(params: GetAvailableSeats$Params, context?: HttpContext): Observable<StrictHttpResponse<AvailableSeatsDto>> {
    return getAvailableSeats(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAvailableSeats$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAvailableSeats(params: GetAvailableSeats$Params, context?: HttpContext): Observable<AvailableSeatsDto> {
    return this.getAvailableSeats$Response(params, context).pipe(
      map((r: StrictHttpResponse<AvailableSeatsDto>): AvailableSeatsDto => r.body)
    );
  }

  /** Path part for operation `getFlightsByFilter()` */
  static readonly GetFlightsByFilterPath = '/api/v1/flights/search';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getFlightsByFilter()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFlightsByFilter$Response(params: GetFlightsByFilter$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFlightDto>> {
    return getFlightsByFilter(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getFlightsByFilter$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getFlightsByFilter(params: GetFlightsByFilter$Params, context?: HttpContext): Observable<PageResponseFlightDto> {
    return this.getFlightsByFilter$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseFlightDto>): PageResponseFlightDto => r.body)
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
  getAllFullFlights$Response(params?: GetAllFullFlights$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<FlightSummaryEmployeeDto>>> {
    return getAllFullFlights(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllFullFlights$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllFullFlights(params?: GetAllFullFlights$Params, context?: HttpContext): Observable<Array<FlightSummaryEmployeeDto>> {
    return this.getAllFullFlights$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<FlightSummaryEmployeeDto>>): Array<FlightSummaryEmployeeDto> => r.body)
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
