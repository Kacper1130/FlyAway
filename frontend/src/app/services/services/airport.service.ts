/* tslint:disable */
/* eslint-disable */
import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';

import {addAirport, AddAirport$Params} from '../fn/airport/add-airport';
import {Airport} from '../models/airport';
import {AirportDto} from '../models/airport-dto';
import {getAllAirports, GetAllAirports$Params} from '../fn/airport/get-all-airports';
import {getAllEnabledAirports, GetAllEnabledAirports$Params} from '../fn/airport/get-all-enabled-airports';
import {switchAirportStatus, SwitchAirportStatus$Params} from '../fn/airport/switch-airport-status';
import {updateAirport, UpdateAirport$Params} from '../fn/airport/update-airport';

@Injectable({ providedIn: 'root' })
export class AirportService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `updateAirport()` */
  static readonly UpdateAirportPath = '/api/v1/airports/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `updateAirport()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateAirport$Response(params: UpdateAirport$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
    return updateAirport(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `updateAirport$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  updateAirport(params: UpdateAirport$Params, context?: HttpContext): Observable<Airport> {
    return this.updateAirport$Response(params, context).pipe(
      map((r: StrictHttpResponse<Airport>): Airport => r.body)
    );
  }

  /** Path part for operation `switchAirportStatus()` */
  static readonly SwitchAirportStatusPath = '/api/v1/airports/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `switchAirportStatus()` instead.
   *
   * This method doesn't expect any request body.
   */
  switchAirportStatus$Response(params: SwitchAirportStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
    return switchAirportStatus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `switchAirportStatus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  switchAirportStatus(params: SwitchAirportStatus$Params, context?: HttpContext): Observable<Airport> {
    return this.switchAirportStatus$Response(params, context).pipe(
      map((r: StrictHttpResponse<Airport>): Airport => r.body)
    );
  }

  /** Path part for operation `addAirport()` */
  static readonly AddAirportPath = '/api/v1/airports/add';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `addAirport()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addAirport$Response(params: AddAirport$Params, context?: HttpContext): Observable<StrictHttpResponse<Airport>> {
    return addAirport(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `addAirport$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  addAirport(params: AddAirport$Params, context?: HttpContext): Observable<Airport> {
    return this.addAirport$Response(params, context).pipe(
      map((r: StrictHttpResponse<Airport>): Airport => r.body)
    );
  }

  /** Path part for operation `getAllAirports()` */
  static readonly GetAllAirportsPath = '/api/v1/airports';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllAirports()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllAirports$Response(params?: GetAllAirports$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<AirportDto>>> {
    return getAllAirports(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllAirports$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllAirports(params?: GetAllAirports$Params, context?: HttpContext): Observable<Array<AirportDto>> {
    return this.getAllAirports$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<AirportDto>>): Array<AirportDto> => r.body)
    );
  }

  /** Path part for operation `getAllEnabledAirports()` */
  static readonly GetAllEnabledAirportsPath = '/api/v1/airports/enabled';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllEnabledAirports()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEnabledAirports$Response(params?: GetAllEnabledAirports$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<AirportDto>>> {
    return getAllEnabledAirports(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllEnabledAirports$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEnabledAirports(params?: GetAllEnabledAirports$Params, context?: HttpContext): Observable<Array<AirportDto>> {
    return this.getAllEnabledAirports$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<AirportDto>>): Array<AirportDto> => r.body)
    );
  }

}
