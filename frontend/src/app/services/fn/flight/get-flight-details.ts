/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { FlightDetailsDto } from '../../models/flight-details-dto';

export interface GetFlightDetails$Params {
  id: string;
}

export function getFlightDetails(http: HttpClient, rootUrl: string, params: GetFlightDetails$Params, context?: HttpContext): Observable<StrictHttpResponse<FlightDetailsDto>> {
  const rb = new RequestBuilder(rootUrl, getFlightDetails.PATH, 'get');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<FlightDetailsDto>;
    })
  );
}

getFlightDetails.PATH = '/api/v1/flights/full/{id}';
