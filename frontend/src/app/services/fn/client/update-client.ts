/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ClientDto } from '../../models/client-dto';

export interface UpdateClient$Params {
      body: ClientDto
}

export function updateClient(http: HttpClient, rootUrl: string, params: UpdateClient$Params, context?: HttpContext): Observable<StrictHttpResponse<ClientDto>> {
  const rb = new RequestBuilder(rootUrl, updateClient.PATH, 'patch');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<ClientDto>;
    })
  );
}

updateClient.PATH = '/api/v1/clients/client';
