/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { AddEmployeeDto } from '../../models/add-employee-dto';

export interface AddEmployee$Params {
  addEmployeeDto: AddEmployeeDto;
}

export function addEmployee(http: HttpClient, rootUrl: string, params: AddEmployee$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
  const rb = new RequestBuilder(rootUrl, addEmployee.PATH, 'post');
  if (params) {
    rb.query('addEmployeeDto', params.addEmployeeDto, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<{
      }>;
    })
  );
}

addEmployee.PATH = '/api/v1/admin/employees/add';
