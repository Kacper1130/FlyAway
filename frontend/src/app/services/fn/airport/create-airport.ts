/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Airport } from '../../models/airport';
import { CreateAirportDto } from '../../models/create-airport-dto';

export interface CreateAirport$Params {
      body: CreateAirportDto
}

export function createAirport(http: HttpClient, rootUrl: string, params: CreateAirport$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
  const rb = new RequestBuilder(rootUrl, createAirport.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
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

createAirport.PATH = '/api/v1/airports';
