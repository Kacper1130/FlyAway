/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Airport } from '../../models/airport';

export interface GetAll2$Params {
}

export function getAll2(http: HttpClient, rootUrl: string, params?: GetAll2$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Airport>>> {
  const rb = new RequestBuilder(rootUrl, getAll2.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<Airport>>;
    })
  );
}

getAll2.PATH = '/api/v1/airports';
