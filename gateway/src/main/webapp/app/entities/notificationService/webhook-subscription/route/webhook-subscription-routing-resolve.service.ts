import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { WebhookSubscriptionService } from '../service/webhook-subscription.service';
import { IWebhookSubscription } from '../webhook-subscription.model';

const webhookSubscriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | IWebhookSubscription> => {
  const id = route.params.id;
  if (id) {
    return inject(WebhookSubscriptionService)
      .find(id)
      .pipe(
        mergeMap((webhookSubscription: HttpResponse<IWebhookSubscription>) => {
          if (webhookSubscription.body) {
            return of(webhookSubscription.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default webhookSubscriptionResolve;
