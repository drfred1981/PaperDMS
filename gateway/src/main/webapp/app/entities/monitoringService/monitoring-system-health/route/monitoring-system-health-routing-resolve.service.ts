import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitoringSystemHealth } from '../monitoring-system-health.model';
import { MonitoringSystemHealthService } from '../service/monitoring-system-health.service';

const monitoringSystemHealthResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitoringSystemHealth> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitoringSystemHealthService)
      .find(id)
      .pipe(
        mergeMap((monitoringSystemHealth: HttpResponse<IMonitoringSystemHealth>) => {
          if (monitoringSystemHealth.body) {
            return of(monitoringSystemHealth.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitoringSystemHealthResolve;
