/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { ClientReservationDto } from '../../models/client-reservation-dto';

export interface GetAllDeletedUsers$Params {
}

export function getAllDeletedUsers(http: HttpClient, rootUrl: string, params?: GetAllDeletedUsers$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ClientReservationDto>>> {
  const rb = new RequestBuilder(rootUrl, getAllDeletedUsers.PATH, 'get');
  if (params) {
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<Array<ClientReservationDto>>;
    })
  );
}

getAllDeletedUsers.PATH = '/api/v1/clients/deleted';
