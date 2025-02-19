/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ReservationDetailsEmployeeDto } from '../../models/reservation-details-employee-dto';

export interface GetReservationDetails1$Params {
  id: string;
}

export function getReservationDetails1(http: HttpClient, rootUrl: string, params: GetReservationDetails1$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDetailsEmployeeDto>> {
  const rb = new RequestBuilder(rootUrl, getReservationDetails1.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ReservationDetailsEmployeeDto>;
    })
  );
}

getReservationDetails1.PATH = '/api/v1/employee/reservations/{id}';
