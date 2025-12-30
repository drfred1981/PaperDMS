import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INotificationTemplate } from '../notification-template.model';
import { NotificationTemplateService } from '../service/notification-template.service';

const notificationTemplateResolve = (route: ActivatedRouteSnapshot): Observable<null | INotificationTemplate> => {
  const id = route.params.id;
  if (id) {
    return inject(NotificationTemplateService)
      .find(id)
      .pipe(
        mergeMap((notificationTemplate: HttpResponse<INotificationTemplate>) => {
          if (notificationTemplate.body) {
            return of(notificationTemplate.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default notificationTemplateResolve;
