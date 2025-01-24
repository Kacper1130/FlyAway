/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { cancelReservation } from '../fn/reservation/cancel-reservation';
import { CancelReservation$Params } from '../fn/reservation/cancel-reservation';
import { createReservation } from '../fn/reservation/create-reservation';
import { CreateReservation$Params } from '../fn/reservation/create-reservation';
import { DisplayReservationDto } from '../models/display-reservation-dto';
import { getALl } from '../fn/reservation/get-a-ll';
import { GetALl$Params } from '../fn/reservation/get-a-ll';
import { ReservationDto } from '../models/reservation-dto';

@Injectable({ providedIn: 'root' })
export class ReservationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `createReservation()` */
  static readonly CreateReservationPath = '/api/v1/reservations/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createReservation()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createReservation$Response(params: CreateReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDto>> {
    return createReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createReservation$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createReservation(params: CreateReservation$Params, context?: HttpContext): Observable<ReservationDto> {
    return this.createReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationDto>): ReservationDto => r.body)
    );
  }

  /** Path part for operation `getALl()` */
  static readonly GetALlPath = '/api/v1/reservations';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getALl()` instead.
   *
   * This method doesn't expect any request body.
   */
  getALl$Response(params?: GetALl$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DisplayReservationDto>>> {
    return getALl(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getALl$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getALl(params?: GetALl$Params, context?: HttpContext): Observable<Array<DisplayReservationDto>> {
    return this.getALl$Response(params, context).pipe(
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
  cancelReservation$Response(params: CancelReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<{
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
}> {
    return this.cancelReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

}
