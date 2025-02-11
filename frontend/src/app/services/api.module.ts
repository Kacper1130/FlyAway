/* tslint:disable */
/* eslint-disable */
import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApiConfiguration, ApiConfigurationParams } from './api-configuration';

import { AirportService } from './services/airport.service';
import { ReservationService } from './services/reservation.service';
import { PaymentControllerService } from './services/payment-controller.service';
import { FlightService } from './services/flight.service';
import { EmployeeService } from './services/employee.service';
import { ClientService } from './services/client.service';
import { AuthenticationService } from './services/authentication.service';
import { AircraftService } from './services/aircraft.service';
import { CountryService } from './services/country.service';
import { UserService } from './services/user.service';

/**
 * Module that provides all services and configuration.
 */
@NgModule({
  imports: [],
  exports: [],
  declarations: [],
  providers: [
    AirportService,
    ReservationService,
    PaymentControllerService,
    FlightService,
    EmployeeService,
    ClientService,
    AuthenticationService,
    AircraftService,
    CountryService,
    UserService,
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
