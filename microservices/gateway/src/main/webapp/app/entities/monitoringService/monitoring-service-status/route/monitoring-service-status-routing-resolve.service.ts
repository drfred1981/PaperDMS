import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitoringServiceStatus } from '../monitoring-service-status.model';
import { MonitoringServiceStatusService } from '../service/monitoring-service-status.service';

const monitoringServiceStatusResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitoringServiceStatus> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitoringServiceStatusService)
      .find(id)
      .pipe(
        mergeMap((monitoringServiceStatus: HttpResponse<IMonitoringServiceStatus>) => {
          if (monitoringServiceStatus.body) {
            return of(monitoringServiceStatus.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitoringServiceStatusResolve;
