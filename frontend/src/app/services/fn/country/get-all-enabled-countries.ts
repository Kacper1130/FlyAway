/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Country } from '../../models/country';

export interface GetAllEnabledCountries$Params {
}

export function getAllEnabledCountries(http: HttpClient, rootUrl: string, params?: GetAllEnabledCountries$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Country>>> {
  const rb = new RequestBuilder(rootUrl, getAllEnabledCountries.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<Country>>;
    })
  );
}

getAllEnabledCountries.PATH = '/api/v1/countries/enabled';
