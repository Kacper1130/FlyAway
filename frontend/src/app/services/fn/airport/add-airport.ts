/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Airport } from '../../models/airport';
import { CreateAirportDto } from '../../models/create-airport-dto';

export interface AddAirport$Params {
      body: CreateAirportDto
}

export function addAirport(http: HttpClient, rootUrl: string, params: AddAirport$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
  const rb = new RequestBuilder(rootUrl, addAirport.PATH, 'post');
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

addAirport.PATH = '/api/v1/airports/add';
