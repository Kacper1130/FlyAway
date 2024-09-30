/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Aircraft } from '../../models/aircraft';

export interface GetAllAircraft$Params {
}

export function getAllAircraft(http: HttpClient, rootUrl: string, params?: GetAllAircraft$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Aircraft>>> {
  const rb = new RequestBuilder(rootUrl, getAllAircraft.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<Aircraft>>;
    })
  );
}

getAllAircraft.PATH = '/api/v1/aircraft';
