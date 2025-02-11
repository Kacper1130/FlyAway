/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DisplayReservationDto } from '../../models/display-reservation-dto';

export interface GetAllReservations$Params {
}

export function getAllReservations(http: HttpClient, rootUrl: string, params?: GetAllReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DisplayReservationDto>>> {
  const rb = new RequestBuilder(rootUrl, getAllReservations.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<DisplayReservationDto>>;
    })
  );
}

getAllReservations.PATH = '/api/v1/reservations/all';
