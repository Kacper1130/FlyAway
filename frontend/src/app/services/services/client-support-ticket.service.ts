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
import {createTicket, CreateTicket$Params} from '../fn/client-support-ticket/create-ticket';
import {getChatMessages, GetChatMessages$Params} from '../fn/client-support-ticket/get-chat-messages';
import {getOwnTickets, GetOwnTickets$Params} from '../fn/client-support-ticket/get-own-tickets';
import {SupportTicket} from '../models/support-ticket';

@Injectable({ providedIn: 'root' })
export class ClientSupportTicketService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `getOwnTickets()` */
  static readonly GetOwnTicketsPath = '/api/v1/tickets';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getOwnTickets()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOwnTickets$Response(params?: GetOwnTickets$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<SupportTicket>>> {
    return getOwnTickets(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getOwnTickets$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getOwnTickets(params?: GetOwnTickets$Params, context?: HttpContext): Observable<Array<SupportTicket>> {
    return this.getOwnTickets$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<SupportTicket>>): Array<SupportTicket> => r.body)
    );
  }

  /** Path part for operation `createTicket()` */
  static readonly CreateTicketPath = '/api/v1/tickets';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `createTicket()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createTicket$Response(params: CreateTicket$Params, context?: HttpContext): Observable<StrictHttpResponse<SupportTicket>> {
    return createTicket(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `createTicket$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  createTicket(params: CreateTicket$Params, context?: HttpContext): Observable<SupportTicket> {
    return this.createTicket$Response(params, context).pipe(
      map((r: StrictHttpResponse<SupportTicket>): SupportTicket => r.body)
    );
  }

  /** Path part for operation `getChatMessages()` */
  static readonly GetChatMessagesPath = '/api/v1/tickets/{ticketId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getChatMessages()` instead.
   *
   * This method doesn't expect any request body.
   */
  getChatMessages$Response(params: GetChatMessages$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ChatMessage>>> {
    return getChatMessages(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getChatMessages$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getChatMessages(params: GetChatMessages$Params, context?: HttpContext): Observable<Array<ChatMessage>> {
    return this.getChatMessages$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ChatMessage>>): Array<ChatMessage> => r.body)
    );
  }

}
