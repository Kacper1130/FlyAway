/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { DisplayReservationDto } from '../../models/display-reservation-dto';

export interface GetALl$Params {
}

export function getALl(http: HttpClient, rootUrl: string, params?: GetALl$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<DisplayReservationDto>>> {
  const rb = new RequestBuilder(rootUrl, getALl.PATH, 'get');
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

getALl.PATH = '/api/v1/reservations';
