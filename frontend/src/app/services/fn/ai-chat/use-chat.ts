/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StrictHttpResponse } from '../../strict-http-response';
import { RequestBuilder } from '../../request-builder';

import { AiChatMessage } from '../../models/ai-chat-message';

export interface UseChat$Params {
      body: AiChatMessage
}

export function useChat(http: HttpClient, rootUrl: string, params: UseChat$Params, context?: HttpContext): Observable<StrictHttpResponse<AiChatMessage>> {
  const rb = new RequestBuilder(rootUrl, useChat.PATH, 'post');
  if (params) {
    rb.body(params.body, 'application/json');
  }

  return http.request(
    rb.build({ responseType: 'json', accept: 'application/json', context })
  ).pipe(
    filter((r: any): r is HttpResponse<any> => r instanceof HttpResponse),
    map((r: HttpResponse<any>) => {
      return r as StrictHttpResponse<AiChatMessage>;
    })
  );
}

useChat.PATH = '/api/v1/ai';
