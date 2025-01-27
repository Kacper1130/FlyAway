/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { CreateFlightDto } from '../../models/create-flight-dto';
import { FlightDto } from '../../models/flight-dto';

export interface Add$Params {
      body: CreateFlightDto
}

export function add(http: HttpClient, rootUrl: string, params: Add$Params, context?: HttpContext): Observable<StrictHttpResponse<FlightDto>> {
  const rb = new RequestBuilder(rootUrl, add.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<FlightDto>;
    })
  );
}

add.PATH = '/api/v1/flights/add';
