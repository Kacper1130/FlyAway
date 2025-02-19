/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SupportTicket } from '../../models/support-ticket';

export interface GetTickets$Params {
}

export function getTickets(http: HttpClient, rootUrl: string, params?: GetTickets$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<SupportTicket>>> {
  const rb = new RequestBuilder(rootUrl, getTickets.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<SupportTicket>>;
    })
  );
}

getTickets.PATH = '/api/v1/employee/tickets';
