/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { FlightSummaryEmployeeDto } from '../../models/flight-summary-employee-dto';

export interface GetAllFullFlights$Params {
}

export function getAllFullFlights(http: HttpClient, rootUrl: string, params?: GetAllFullFlights$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<FlightSummaryEmployeeDto>>> {
  const rb = new RequestBuilder(rootUrl, getAllFullFlights.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<FlightSummaryEmployeeDto>>;
    })
  );
}

getAllFullFlights.PATH = '/api/v1/flights/full';
