/* tslint:disable */
/* eslint-disable */
import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';

import {ChatMessage} from '../models/chat-message';
import {getChatMessages1, GetChatMessages1$Params} from '../fn/employee-support-ticket/get-chat-messages-1';
import {getTickets, GetTickets$Params} from '../fn/employee-support-ticket/get-tickets';
import {SupportTicket} from '../models/support-ticket';

@Injectable({ providedIn: 'root' })
export class EmployeeSupportTicketService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getTickets()` */
  static readonly GetTicketsPath = '/api/v1/employee/tickets';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getTickets()` instead.
   *
   * This method doesn't expect any request body.
   */
  getTickets$Response(params?: GetTickets$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<SupportTicket>>> {
    return getTickets(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getTickets$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getTickets(params?: GetTickets$Params, context?: HttpContext): Observable<Array<SupportTicket>> {
    return this.getTickets$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<SupportTicket>>): Array<SupportTicket> => r.body)
    );
  }

  /** Path part for operation `getChatMessages1()` */
  static readonly GetChatMessages1Path = '/api/v1/employee/tickets/{ticketId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getChatMessages1()` instead.
   *
   * This method doesn't expect any request body.
   */
  getChatMessages1$Response(params: GetChatMessages1$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ChatMessage>>> {
    return getChatMessages1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getChatMessages1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getChatMessages1(params: GetChatMessages1$Params, context?: HttpContext): Observable<Array<ChatMessage>> {
    return this.getChatMessages1$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ChatMessage>>): Array<ChatMessage> => r.body)
    );
  }

}
