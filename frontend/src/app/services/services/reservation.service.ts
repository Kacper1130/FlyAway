/* tslint:disable */
/* eslint-disable */
import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';

import {cancelOwnReservation, CancelOwnReservation$Params} from '../fn/reservation/cancel-own-reservation';
import {cancelReservation, CancelReservation$Params} from '../fn/reservation/cancel-reservation';
import {createReservation, CreateReservation$Params} from '../fn/reservation/create-reservation';
import {DisplayReservationDto} from '../models/display-reservation-dto';
import {getAllReservations, GetAllReservations$Params} from '../fn/reservation/get-all-reservations';
import {getOwnReservations, GetOwnReservations$Params} from '../fn/reservation/get-own-reservations';
import {getReservationDetails, GetReservationDetails$Params} from '../fn/reservation/get-reservation-details';
import {ReservationDto} from '../models/reservation-dto';
import {ReservationPaymentResponseDto} from '../models/reservation-payment-response-dto';

@Injectable({ providedIn: 'root' })
export class ReservationService extends BaseService {
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
  getOwnReservations$Response(params?: GetOwnReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ReservationDto>>> {
    return getOwnReservations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getOwnReservations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOwnReservations(params?: GetOwnReservations$Params, context?: HttpContext): Observable<Array<ReservationDto>> {
    return this.getOwnReservations$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ReservationDto>>): Array<ReservationDto> => r.body)
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
  getReservationDetails$Response(params: GetReservationDetails$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDto>> {
    return getReservationDetails(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getReservationDetails$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getReservationDetails(params: GetReservationDetails$Params, context?: HttpContext): Observable<ReservationDto> {
    return this.getReservationDetails$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationDto>): ReservationDto => r.body)
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
  cancelOwnReservation$Response(params: CancelOwnReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return cancelOwnReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelOwnReservation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelOwnReservation(params: CancelOwnReservation$Params, context?: HttpContext): Observable<string> {
    return this.cancelOwnReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

  /** Path part for operation `getAllReservations()` */
  static readonly GetAllReservationsPath = '/api/v1/reservations/all';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllReservations()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllReservations$Response(params?: GetAllReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DisplayReservationDto>>> {
    return getAllReservations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllReservations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllReservations(params?: GetAllReservations$Params, context?: HttpContext): Observable<Array<DisplayReservationDto>> {
    return this.getAllReservations$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<DisplayReservationDto>>): Array<DisplayReservationDto> => r.body)
    );
  }

  /** Path part for operation `cancelReservation()` */
  static readonly CancelReservationPath = '/api/v1/reservations/{id}/cancel';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `cancelReservation()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelReservation$Response(params: CancelReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return cancelReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelReservation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelReservation(params: CancelReservation$Params, context?: HttpContext): Observable<string> {
    return this.cancelReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
