/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { cancelOwnReservation } from '../fn/client-reservation/cancel-own-reservation';
import { CancelOwnReservation$Params } from '../fn/client-reservation/cancel-own-reservation';
import { createReservation } from '../fn/client-reservation/create-reservation';
import { CreateReservation$Params } from '../fn/client-reservation/create-reservation';
import { getOwnReservations } from '../fn/client-reservation/get-own-reservations';
import { GetOwnReservations$Params } from '../fn/client-reservation/get-own-reservations';
import { getReservationDetails } from '../fn/client-reservation/get-reservation-details';
import { GetReservationDetails$Params } from '../fn/client-reservation/get-reservation-details';
import { ReservationDetailsClientDto } from '../models/reservation-details-client-dto';
import { ReservationPaymentResponseDto } from '../models/reservation-payment-response-dto';
import { ReservationSummaryClientDto } from '../models/reservation-summary-client-dto';

@Injectable({ providedIn: 'root' })
export class ClientReservationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createReservation()` */
  static readonly CreateReservationPath = '/api/v1/reservations/create';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createReservation()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createReservation$Response(params: CreateReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationPaymentResponseDto>> {
    return createReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createReservation$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createReservation(params: CreateReservation$Params, context?: HttpContext): Observable<ReservationPaymentResponseDto> {
    return this.createReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationPaymentResponseDto>): ReservationPaymentResponseDto => r.body)
    );
  }

  /** Path part for operation `getOwnReservations()` */
  static readonly GetOwnReservationsPath = '/api/v1/reservations';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOwnReservations()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOwnReservations$Response(params?: GetOwnReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ReservationSummaryClientDto>>> {
    return getOwnReservations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getOwnReservations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOwnReservations(params?: GetOwnReservations$Params, context?: HttpContext): Observable<Array<ReservationSummaryClientDto>> {
    return this.getOwnReservations$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ReservationSummaryClientDto>>): Array<ReservationSummaryClientDto> => r.body)
    );
  }

  /** Path part for operation `getReservationDetails()` */
  static readonly GetReservationDetailsPath = '/api/v1/reservations/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getReservationDetails()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservationDetails$Response(params: GetReservationDetails$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDetailsClientDto>> {
    return getReservationDetails(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getReservationDetails$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservationDetails(params: GetReservationDetails$Params, context?: HttpContext): Observable<ReservationDetailsClientDto> {
    return this.getReservationDetails$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationDetailsClientDto>): ReservationDetailsClientDto => r.body)
    );
  }

  /** Path part for operation `cancelOwnReservation()` */
  static readonly CancelOwnReservationPath = '/api/v1/reservations/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `cancelOwnReservation()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelOwnReservation$Response(params: CancelOwnReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<{
[key: string]: string;
}>> {
    return cancelOwnReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelOwnReservation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelOwnReservation(params: CancelOwnReservation$Params, context?: HttpContext): Observable<{
[key: string]: string;
}> {
    return this.cancelOwnReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
[key: string]: string;
}>): {
[key: string]: string;
} => r.body)
    );
  }

}
