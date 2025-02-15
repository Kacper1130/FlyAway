/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CreateReservationDto } from '../../models/create-reservation-dto';
import { ReservationPaymentResponseDto } from '../../models/reservation-payment-response-dto';

export interface CreateReservation$Params {
      body: CreateReservationDto
}

export function createReservation(http: HttpClient, rootUrl: string, params: CreateReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationPaymentResponseDto>> {
  const rb = new RequestBuilder(rootUrl, createReservation.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ReservationPaymentResponseDto>;
    })
  );
}

createReservation.PATH = '/api/v1/reservations/create';
