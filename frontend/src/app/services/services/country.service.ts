/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { Country } from '../models/country';
import { getAllCountries } from '../fn/country/get-all-countries';
import { GetAllCountries$Params } from '../fn/country/get-all-countries';
import { switchCountryStatus } from '../fn/country/switch-country-status';
import { SwitchCountryStatus$Params } from '../fn/country/switch-country-status';

@Injectable({ providedIn: 'root' })
export class CountryService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `switchCountryStatus()` */
  static readonly SwitchCountryStatusPath = '/api/v1/countries/{id}';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `switchCountryStatus()` instead.
   *
   * This method doesn't expect any request body.
   */
  switchCountryStatus$Response(params: SwitchCountryStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<Country>> {
    return switchCountryStatus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `switchCountryStatus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  switchCountryStatus(params: SwitchCountryStatus$Params, context?: HttpContext): Observable<Country> {
    return this.switchCountryStatus$Response(params, context).pipe(
      map((r: StrictHttpResponse<Country>): Country => r.body)
    );
  }

  /** Path part for operation `getAllCountries()` */
  static readonly GetAllCountriesPath = '/api/v1/countries';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllCountries()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllCountries$Response(params?: GetAllCountries$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Country>>> {
    return getAllCountries(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllCountries$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllCountries(params?: GetAllCountries$Params, context?: HttpContext): Observable<Array<Country>> {
    return this.getAllCountries$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Country>>): Array<Country> => r.body)
    );
  }

}
