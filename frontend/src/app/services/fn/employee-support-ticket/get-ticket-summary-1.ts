/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { SupportTicketSummaryDto } from '../../models/support-ticket-summary-dto';

export interface GetTicketSummary1$Params {
  ticketId: string;
}

export function getTicketSummary1(http: HttpClient, rootUrl: string, params: GetTicketSummary1$Params, context?: HttpContext): Observable<StrictHttpResponse<SupportTicketSummaryDto>> {
  const rb = new RequestBuilder(rootUrl, getTicketSummary1.PATH, 'get');
  if (params) {
    rb.path('ticketId', params.ticketId, {});
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<SupportTicketSummaryDto>;
    })
  );
}

getTicketSummary1.PATH = '/api/v1/employee/tickets/{ticketId}/summary';
