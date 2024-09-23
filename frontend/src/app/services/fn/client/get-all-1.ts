/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ClientDto } from '../../models/client-dto';

export interface GetAll1$Params {
}

export function getAll1(http: HttpClient, rootUrl: string, params?: GetAll1$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ClientDto>>> {
  const rb = new RequestBuilder(rootUrl, getAll1.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<ClientDto>>;
    })
  );
}

getAll1.PATH = '/api/v1/clients';
