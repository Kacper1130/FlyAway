/* tslint:disable */
/* eslint-disable */
import {HttpClient, HttpContext} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {BaseService} from '../base-service';
import {ApiConfiguration} from '../api-configuration';
import {StrictHttpResponse} from '../strict-http-response';

import {Country} from '../models/country';
import {CountryDto} from '../models/country-dto';
import {getAllCountries, GetAllCountries$Params} from '../fn/country/get-all-countries';
import {getAllEnabledCountries, GetAllEnabledCountries$Params} from '../fn/country/get-all-enabled-countries';
import {switchCountryStatus, SwitchCountryStatus$Params} from '../fn/country/switch-country-status';

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
  switchCountryStatus$Response(params: SwitchCountryStatus$Params, context?: HttpContext): Observable<StrictHttpResponse<CountryDto>> {
    return switchCountryStatus(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `switchCountryStatus$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  switchCountryStatus(params: SwitchCountryStatus$Params, context?: HttpContext): Observable<CountryDto> {
    return this.switchCountryStatus$Response(params, context).pipe(
      map((r: StrictHttpResponse<CountryDto>): CountryDto => r.body)
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
  getAllCountries$Response(params?: GetAllCountries$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<CountryDto>>> {
    return getAllCountries(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllCountries$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllCountries(params?: GetAllCountries$Params, context?: HttpContext): Observable<Array<CountryDto>> {
    return this.getAllCountries$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<CountryDto>>): Array<CountryDto> => r.body)
    );
  }

  /** Path part for operation `getAllEnabledCountries()` */
  static readonly GetAllEnabledCountriesPath = '/api/v1/countries/enabled-countries';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `getAllEnabledCountries()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEnabledCountries$Response(params?: GetAllEnabledCountries$Params, context?: HttpContext): Observable<StrictHttpResponse<Array<Country>>> {
    return getAllEnabledCountries(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `getAllEnabledCountries$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  getAllEnabledCountries(params?: GetAllEnabledCountries$Params, context?: HttpContext): Observable<Array<Country>> {
    return this.getAllEnabledCountries$Response(params, context).pipe(
      map((r: StrictHttpResponse<Array<Country>>): Array<Country> => r.body)
    );
  }

}
