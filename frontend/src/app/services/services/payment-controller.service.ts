/* tslint:disable */
/* eslint-disable */
import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { handleStripeWebhook } from '../fn/payment-controller/handle-stripe-webhook';
import { HandleStripeWebhook$Params } from '../fn/payment-controller/handle-stripe-webhook';

@Injectable({ providedIn: 'root' })
export class PaymentControllerService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `handleStripeWebhook()` */
  static readonly HandleStripeWebhookPath = '/api/v1/payment/webhook';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `handleStripeWebhook()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  handleStripeWebhook$Response(params: HandleStripeWebhook$Params, context?: HttpContext): Observable<StrictHttpResponse<string>> {
    return handleStripeWebhook(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `handleStripeWebhook$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  handleStripeWebhook(params: HandleStripeWebhook$Params, context?: HttpContext): Observable<string> {
    return this.handleStripeWebhook$Response(params, context).pipe(
      map((r: StrictHttpResponse<string>): string => r.body)
    );
  }

}
