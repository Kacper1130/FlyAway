/* tslint:disable */
/* eslint-disable */
import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';

import {cancelReservation, CancelReservation$Params} from '../fn/employee-reservation/cancel-reservation';
import {
  getReservationDetails1,
  GetReservationDetails1$Params
} from '../fn/employee-reservation/get-reservation-details-1';
import {getReservations, GetReservations$Params} from '../fn/employee-reservation/get-reservations';
import {PageResponseReservationSummaryEmployeeDto} from '../models/page-response-reservation-summary-employee-dto';
import {ReservationDto} from '../models/reservation-dto';
import {ReservationSummaryEmployeeDto} from '../models/reservation-summary-employee-dto';
import {searchReservationById, SearchReservationById$Params} from '../fn/employee-reservation/search-reservation-by-id';

@Injectable({ providedIn: 'root' })
export class EmployeeReservationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getReservations()` */
  static readonly GetReservationsPath = '/api/v1/employee/reservations';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getReservations()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservations$Response(params?: GetReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseReservationSummaryEmployeeDto>> {
    return getReservations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getReservations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservations(params?: GetReservations$Params, context?: HttpContext): Observable<PageResponseReservationSummaryEmployeeDto> {
    return this.getReservations$Response(params, context).pipe(
      map((r: StrictHttpResponse<PageResponseReservationSummaryEmployeeDto>): PageResponseReservationSummaryEmployeeDto => r.body)
    );
  }

  /** Path part for operation `getReservationDetails1()` */
  static readonly GetReservationDetails1Path = '/api/v1/employee/reservations/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getReservationDetails1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservationDetails1$Response(params: GetReservationDetails1$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDto>> {
    return getReservationDetails1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getReservationDetails1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservationDetails1(params: GetReservationDetails1$Params, context?: HttpContext): Observable<ReservationDto> {
    return this.getReservationDetails1$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationDto>): ReservationDto => r.body)
    );
  }

  /** Path part for operation `cancelReservation()` */
  static readonly CancelReservationPath = '/api/v1/employee/reservations/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `cancelReservation()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelReservation$Response(params: CancelReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<{
[key: string]: string;
}>> {
    return cancelReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelReservation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelReservation(params: CancelReservation$Params, context?: HttpContext): Observable<{
[key: string]: string;
}> {
    return this.cancelReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
[key: string]: string;
}>): {
[key: string]: string;
} => r.body)
    );
  }

  /** Path part for operation `searchReservationById()` */
  static readonly SearchReservationByIdPath = '/api/v1/employee/reservations/search';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `searchReservationById()` instead.
   *
   * This method doesn't expect any request body.
   */
  searchReservationById$Response(params: SearchReservationById$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationSummaryEmployeeDto>> {
    return searchReservationById(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `searchReservationById$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  searchReservationById(params: SearchReservationById$Params, context?: HttpContext): Observable<ReservationSummaryEmployeeDto> {
    return this.searchReservationById$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationSummaryEmployeeDto>): ReservationSummaryEmployeeDto => r.body)
    );
  }

}
