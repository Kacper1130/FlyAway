/* tslint:disable */
/* eslint-disable */
import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';

import {add1, Add1$Params} from '../fn/client/add-1';
import {cancelReservation1, CancelReservation1$Params} from '../fn/client/cancel-reservation-1';
import {ClientDto} from '../models/client-dto';
import {ClientReservationDto} from '../models/client-reservation-dto';
import {deleteClient, DeleteClient$Params} from '../fn/client/delete-client';
import {getAll, GetAll$Params} from '../fn/client/get-all';
import {getAllDeletedUsers, GetAllDeletedUsers$Params} from '../fn/client/get-all-deleted-users';
import {getClient, GetClient$Params} from '../fn/client/get-client';
import {getClientFromId, GetClientFromId$Params} from '../fn/client/get-client-from-id';
import {getClientReservation, GetClientReservation$Params} from '../fn/client/get-client-reservation';
import {getClientWithReservations, GetClientWithReservations$Params} from '../fn/client/get-client-with-reservations';
import {ReservationDto} from '../models/reservation-dto';

@Injectable({ providedIn: 'root' })
export class ClientService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `add1()` */
  static readonly Add1Path = '/api/v1/clients/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `add1()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  add1$Response(params: Add1$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return add1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `add1$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  add1(params: Add1$Params, context?: HttpContext): Observable<{
}> {
    return this.add1$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `getAll()` */
  static readonly GetAllPath = '/api/v1/clients';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAll()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll$Response(params?: GetAll$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ClientDto>>> {
    return getAll(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAll$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAll(params?: GetAll$Params, context?: HttpContext): Observable<Array<ClientDto>> {
    return this.getAll$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ClientDto>>): Array<ClientDto> => r.body)
    );
  }

  /** Path part for operation `getClientFromId()` */
  static readonly GetClientFromIdPath = '/api/v1/clients/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getClientFromId()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClientFromId$Response(params: GetClientFromId$Params, context?: HttpContext): Observable<StrictHttpResponse<ClientDto>> {
    return getClientFromId(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getClientFromId$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClientFromId(params: GetClientFromId$Params, context?: HttpContext): Observable<ClientDto> {
    return this.getClientFromId$Response(params, context).pipe(
      map((r: StrictHttpResponse<ClientDto>): ClientDto => r.body)
    );
  }

  /** Path part for operation `deleteClient()` */
  static readonly DeleteClientPath = '/api/v1/clients/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `deleteClient()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteClient$Response(params: DeleteClient$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return deleteClient(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `deleteClient$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  deleteClient(params: DeleteClient$Params, context?: HttpContext): Observable<{
}> {
    return this.deleteClient$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `getClientWithReservations()` */
  static readonly GetClientWithReservationsPath = '/api/v1/clients/{id}/reservations';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getClientWithReservations()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClientWithReservations$Response(params: GetClientWithReservations$Params, context?: HttpContext): Observable<StrictHttpResponse<ClientReservationDto>> {
    return getClientWithReservations(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getClientWithReservations$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClientWithReservations(params: GetClientWithReservations$Params, context?: HttpContext): Observable<ClientReservationDto> {
    return this.getClientWithReservations$Response(params, context).pipe(
      map((r: StrictHttpResponse<ClientReservationDto>): ClientReservationDto => r.body)
    );
  }

  /** Path part for operation `getClientReservation()` */
  static readonly GetClientReservationPath = '/api/v1/clients/{id}/reservations/{reservationId}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getClientReservation()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClientReservation$Response(params: GetClientReservation$Params, context?: HttpContext): Observable<StrictHttpResponse<ReservationDto>> {
    return getClientReservation(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getClientReservation$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClientReservation(params: GetClientReservation$Params, context?: HttpContext): Observable<ReservationDto> {
    return this.getClientReservation$Response(params, context).pipe(
      map((r: StrictHttpResponse<ReservationDto>): ReservationDto => r.body)
    );
  }

  /** Path part for operation `getAllDeletedUsers()` */
  static readonly GetAllDeletedUsersPath = '/api/v1/clients/deleted';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllDeletedUsers()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllDeletedUsers$Response(params?: GetAllDeletedUsers$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<ClientReservationDto>>> {
    return getAllDeletedUsers(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllDeletedUsers$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllDeletedUsers(params?: GetAllDeletedUsers$Params, context?: HttpContext): Observable<Array<ClientReservationDto>> {
    return this.getAllDeletedUsers$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<ClientReservationDto>>): Array<ClientReservationDto> => r.body)
    );
  }

  /** Path part for operation `getClient()` */
  static readonly GetClientPath = '/api/v1/clients/client';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getClient()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClient$Response(params?: GetClient$Params, context?: HttpContext): Observable<StrictHttpResponse<ClientDto>> {
    return getClient(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getClient$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getClient(params?: GetClient$Params, context?: HttpContext): Observable<ClientDto> {
    return this.getClient$Response(params, context).pipe(
      map((r: StrictHttpResponse<ClientDto>): ClientDto => r.body)
    );
  }

  /** Path part for operation `cancelReservation1()` */
  static readonly CancelReservation1Path = '/api/v1/clients/{userId}/reservations/{reservationId}/cancel';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `cancelReservation1()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelReservation1$Response(params: CancelReservation1$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return cancelReservation1(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `cancelReservation1$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  cancelReservation1(params: CancelReservation1$Params, context?: HttpContext): Observable<{
}> {
    return this.cancelReservation1$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

}
