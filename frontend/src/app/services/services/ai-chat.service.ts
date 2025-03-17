/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { useChat } from '../fn/ai-chat/use-chat';
import { UseChat$Params } from '../fn/ai-chat/use-chat';

@Injectable({ providedIn: 'root' })
export class AiChatService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `useChat()` */
  static readonly UseChatPath = '/api/v1/ai';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `useChat()` instead.
   *
   * This method doesn't expect any request body.
   */
  useChat$Response(params: UseChat$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return useChat(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `useChat$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  useChat(params: UseChat$Params, context?: HttpContext): Observable<string> {
    return this.useChat$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
