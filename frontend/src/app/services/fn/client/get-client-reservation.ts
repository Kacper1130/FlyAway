/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ReservationDto } from '../../models/reservation-dto';

export interface GetClientReservation$Params {
  id: number;
  reservationId: string;
}

export function getClientReservation(http: HttpClient, rootUrl: string, params: GetClientReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDto>> {
  const rb = new RequestBuilder(rootUrl, getClientReservation.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
    rb.path('reservationId', params.reservationId, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ReservationDto>;
    })
  );
}

getClientReservation.PATH = '/api/v1/clients/{id}/reservations/{reservationId}';
