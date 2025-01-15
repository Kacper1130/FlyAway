/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { PageResponseFlightDto } from '../../models/page-response-flight-dto';

export interface GetFlightsByFilter$Params {
  filters: {
[key: string]: {
};
};
  page?: number;
  size?: number;
}

export function getFlightsByFilter(http: HttpClient, rootUrl: string, params: GetFlightsByFilter$Params, context?: HttpContext): Observable<StrictHttpResponse<PageResponseFlightDto>> {
  const rb = new RequestBuilder(rootUrl, getFlightsByFilter.PATH, 'get');
  if (params) {
    rb.query('filters', params.filters, {});
    rb.query('page', params.page, {});
    rb.query('size', params.size, {});
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

getFlightsByFilter.PATH = '/api/v1/flights/search';
