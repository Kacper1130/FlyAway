/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseFlightDto } from '../../models/page-response-flight-dto';

export interface GetFlights$Params {
  page?: number;
  size?: number;
  departureCountry?: string;
}

export function getFlights(http: HttpClient, rootUrl: string, params?: GetFlights$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFlightDto>> {
  const rb = new RequestBuilder(rootUrl, getFlights.PATH, 'get');
  if (params) {
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
    rb.query('departureCountry', params.departureCountry, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<PageResponseFlightDto>;
    })
  );
}

getFlights.PATH = '/api/v1/flights';
