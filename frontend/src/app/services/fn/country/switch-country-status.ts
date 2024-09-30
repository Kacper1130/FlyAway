/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Country } from '../../models/country';

export interface SwitchCountryStatus$Params {
  id: number;
}

export function switchCountryStatus(http: HttpClient, rootUrl: string, params: SwitchCountryStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<Country>> {
  const rb = new RequestBuilder(rootUrl, switchCountryStatus.PATH, 'patch');
  if (params) {
    rb.path('id', params.id, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Country>;
    })
  );
}

switchCountryStatus.PATH = '/api/v1/countries/{id}';
