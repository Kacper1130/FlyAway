/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ReservationSummaryEmployeeDto } from '../../models/reservation-summary-employee-dto';

export interface SearchReservationById$Params {
  id: string;
}

export function searchReservationById(http: HttpClient, rootUrl: string, params: SearchReservationById$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationSummaryEmployeeDto>> {
  const rb = new RequestBuilder(rootUrl, searchReservationById.PATH, 'get');
  if (params) {
    rb.query('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ReservationSummaryEmployeeDto>;
    })
  );
}

searchReservationById.PATH = '/api/v1/employee/reservations/search';
