/* tslint:disable */
/* eslint-disable */
import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';

import { AirportService } from './services/airport.service';
import { ClientReservationService } from './services/client-reservation.service';
import { FlightService } from './services/flight.service';
import { EmployeeService } from './services/employee.service';
import { ClientService } from './services/client.service';
import { AuthenticationService } from './services/authentication.service';
import { AircraftService } from './services/aircraft.service';
import { CountryService } from './services/country.service';
import { EmployeeReservationService } from './services/employee-reservation.service';

/**
 * Module that provides all services and configuration.
 */
@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: [
    AirportService,
    ClientReservationService,
    FlightService,
    EmployeeService,
    ClientService,
    AuthenticationService,
    AircraftService,
    CountryService,
    EmployeeReservationService,
    ApiConfiguration
  ],
})
export class ApiModule {
  static forRoot(params: ApiConfigurationParams): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [
        {
          provide: ApiConfiguration,
          useValue: params
        }
      ]
    }
  }

  constructor( 
    @Optional() @SkipSelf() parentModule: ApiModule,
    @Optional() http: HttpClient
  ) {
    if (parentModule) {
      throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
    }
    if (!http) {
      throw new Error('You need to import the HttpClientModule in your AppModule! \n' +
      'See also https://github.com/angular/angular/issues/20575');
    }
  }
}
