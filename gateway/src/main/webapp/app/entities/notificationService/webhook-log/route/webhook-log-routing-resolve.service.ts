import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWebhookLog } from '../webhook-log.model';
import { WebhookLogService } from '../service/webhook-log.service';

const webhookLogResolve = (route: ActivatedRouteSnapshot): Observable<null | IWebhookLog> => {
  const id = route.params.id;
  if (id) {
    return inject(WebhookLogService)
      .find(id)
      .pipe(
        mergeMap((webhookLog: HttpResponse<IWebhookLog>) => {
          if (webhookLog.body) {
            return of(webhookLog.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default webhookLogResolve;
