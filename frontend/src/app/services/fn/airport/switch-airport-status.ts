/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Airport } from '../../models/airport';

export interface SwitchAirportStatus$Params {
  id: string;
}

export function switchAirportStatus(http: HttpClient, rootUrl: string, params: SwitchAirportStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
  const rb = new RequestBuilder(rootUrl, switchAirportStatus.PATH, 'patch');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Airport>;
    })
  );
}

switchAirportStatus.PATH = '/api/v1/airports/{id}';
