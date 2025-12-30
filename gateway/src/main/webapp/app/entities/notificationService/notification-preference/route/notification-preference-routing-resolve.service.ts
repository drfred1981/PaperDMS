import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationPreference } from '../notification-preference.model';
import { NotificationPreferenceService } from '../service/notification-preference.service';

const notificationPreferenceResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificationPreference> => {
  const id = route.params.id;
  if (id) {
    return inject(NotificationPreferenceService)
      .find(id)
      .pipe(
        mergeMap((notificationPreference: HttpResponse<INotificationPreference>) => {
          if (notificationPreference.body) {
            return of(notificationPreference.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default notificationPreferenceResolve;
