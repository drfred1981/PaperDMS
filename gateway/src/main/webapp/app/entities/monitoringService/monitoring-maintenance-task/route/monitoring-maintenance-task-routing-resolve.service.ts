import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitoringMaintenanceTask } from '../monitoring-maintenance-task.model';
import { MonitoringMaintenanceTaskService } from '../service/monitoring-maintenance-task.service';

const monitoringMaintenanceTaskResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitoringMaintenanceTask> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitoringMaintenanceTaskService)
      .find(id)
      .pipe(
        mergeMap((monitoringMaintenanceTask: HttpResponse<IMonitoringMaintenanceTask>) => {
          if (monitoringMaintenanceTask.body) {
            return of(monitoringMaintenanceTask.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitoringMaintenanceTaskResolve;
