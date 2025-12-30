import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationWebhookLog } from '../notification-webhook-log.model';
import { NotificationWebhookLogService } from '../service/notification-webhook-log.service';

const notificationWebhookLogResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificationWebhookLog> => {
  const id = route.params.id;
  if (id) {
    return inject(NotificationWebhookLogService)
      .find(id)
      .pipe(
        mergeMap((notificationWebhookLog: HttpResponse<INotificationWebhookLog>) => {
          if (notificationWebhookLog.body) {
            return of(notificationWebhookLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default notificationWebhookLogResolve;
