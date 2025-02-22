/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { ChatMessage } from '../models/chat-message';
import { createTicket } from '../fn/client-support-ticket/create-ticket';
import { CreateTicket$Params } from '../fn/client-support-ticket/create-ticket';
import { getChatMessages } from '../fn/client-support-ticket/get-chat-messages';
import { GetChatMessages$Params } from '../fn/client-support-ticket/get-chat-messages';
import { getOwnTickets } from '../fn/client-support-ticket/get-own-tickets';
import { GetOwnTickets$Params } from '../fn/client-support-ticket/get-own-tickets';
import { getTicketSummary } from '../fn/client-support-ticket/get-ticket-summary';
import { GetTicketSummary$Params } from '../fn/client-support-ticket/get-ticket-summary';
import { SupportTicket } from '../models/support-ticket';
import { SupportTicketSummaryDto } from '../models/support-ticket-summary-dto';

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

  /** Path part for operation `getTicketSummary()` */
  static readonly GetTicketSummaryPath = '/api/v1/tickets/{ticketId}/summary';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getTicketSummary()` instead.
   *
   * This method doesn't expect any request body.
   */
  getTicketSummary$Response(params: GetTicketSummary$Params, context?: HttpContext): Observable<StrictHttpResponse<SupportTicketSummaryDto>> {
    return getTicketSummary(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getTicketSummary$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getTicketSummary(params: GetTicketSummary$Params, context?: HttpContext): Observable<SupportTicketSummaryDto> {
    return this.getTicketSummary$Response(params, context).pipe(
      map((r: StrictHttpResponse<SupportTicketSummaryDto>): SupportTicketSummaryDto => r.body)
    );
  }

}
