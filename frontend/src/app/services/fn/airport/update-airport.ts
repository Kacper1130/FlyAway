/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Airport } from '../../models/airport';
import { CreateAirportDto } from '../../models/create-airport-dto';

export interface UpdateAirport$Params {
  id: string;
      body: CreateAirportDto
}

export function updateAirport(http: HttpClient, rootUrl: string, params: UpdateAirport$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
  const rb = new RequestBuilder(rootUrl, updateAirport.PATH, 'put');
  if (params) {
    rb.path('id', params.id, {});
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

updateAirport.PATH = '/api/v1/airports/{id}';
