/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { AvailableSeatsDto } from '../../models/available-seats-dto';

export interface GetAvailableSeats$Params {
  id: string;
  'cabin-class': string;
}

export function getAvailableSeats(http: HttpClient, rootUrl: string, params: GetAvailableSeats$Params, context?: HttpContext): Observable<StrictHttpResponse<AvailableSeatsDto>> {
  const rb = new RequestBuilder(rootUrl, getAvailableSeats.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
    rb.path('cabin-class', params['cabin-class'], {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<AvailableSeatsDto>;
    })
  );
}

getAvailableSeats.PATH = '/api/v1/flights/{id}/available-seats/{cabin-class}';
