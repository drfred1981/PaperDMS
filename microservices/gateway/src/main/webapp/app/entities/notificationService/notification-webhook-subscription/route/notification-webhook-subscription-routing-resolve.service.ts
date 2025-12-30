import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationWebhookSubscription } from '../notification-webhook-subscription.model';
import { NotificationWebhookSubscriptionService } from '../service/notification-webhook-subscription.service';

const notificationWebhookSubscriptionResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificationWebhookSubscription> => {
  const id = route.params.id;
  if (id) {
    return inject(NotificationWebhookSubscriptionService)
      .find(id)
      .pipe(
        mergeMap((notificationWebhookSubscription: HttpResponse<INotificationWebhookSubscription>) => {
          if (notificationWebhookSubscription.body) {
            return of(notificationWebhookSubscription.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default notificationWebhookSubscriptionResolve;
