/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Aircraft } from '../../models/aircraft';

export interface CreateAircraft$Params {
      body: Aircraft
}

export function createAircraft(http: HttpClient, rootUrl: string, params: CreateAircraft$Params, context?: HttpContext): Observable<StrictHttpResponse<Aircraft>> {
  const rb = new RequestBuilder(rootUrl, createAircraft.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Aircraft>;
    })
  );
}

createAircraft.PATH = '/api/v1/aircraft';
