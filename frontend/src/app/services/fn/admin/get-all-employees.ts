/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { Employee } from '../../models/employee';

export interface GetAllEmployees$Params {
}

export function getAllEmployees(http: HttpClient, rootUrl: string, params?: GetAllEmployees$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Employee>>> {
  const rb = new RequestBuilder(rootUrl, getAllEmployees.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<Employee>>;
    })
  );
}

getAllEmployees.PATH = '/api/v1/admin/employees';
