import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationEvent } from '../notification-event.model';
import { NotificationEventService } from '../service/notification-event.service';

const notificationEventResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificationEvent> => {
  const id = route.params.id;
  if (id) {
    return inject(NotificationEventService)
      .find(id)
      .pipe(
        mergeMap((notificationEvent: HttpResponse<INotificationEvent>) => {
          if (notificationEvent.body) {
            return of(notificationEvent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default notificationEventResolve;
