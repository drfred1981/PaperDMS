import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitoringAlert } from '../monitoring-alert.model';
import { MonitoringAlertService } from '../service/monitoring-alert.service';

const monitoringAlertResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitoringAlert> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitoringAlertService)
      .find(id)
      .pipe(
        mergeMap((monitoringAlert: HttpResponse<IMonitoringAlert>) => {
          if (monitoringAlert.body) {
            return of(monitoringAlert.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitoringAlertResolve;
