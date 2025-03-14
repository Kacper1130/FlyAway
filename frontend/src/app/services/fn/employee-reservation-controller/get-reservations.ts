/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseDisplayReservationDto } from '../../models/page-response-display-reservation-dto';

export interface GetReservations$Params {
  page?: number;
  size?: number;
}

export function getReservations(http: HttpClient, rootUrl: string, params?: GetReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseDisplayReservationDto>> {
  const rb = new RequestBuilder(rootUrl, getReservations.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseDisplayReservationDto>;
    })
  );
}

getReservations.PATH = '/api/v1/employee/reservations';
