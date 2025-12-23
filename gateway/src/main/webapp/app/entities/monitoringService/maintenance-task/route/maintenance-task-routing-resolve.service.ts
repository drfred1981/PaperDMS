import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMaintenanceTask } from '../maintenance-task.model';
import { MaintenanceTaskService } from '../service/maintenance-task.service';

const maintenanceTaskResolve = (route: ActivatedRouteSnapshot): Observable<null | IMaintenanceTask> => {
  const id = route.params.id;
  if (id) {
    return inject(MaintenanceTaskService)
      .find(id)
      .pipe(
        mergeMap((maintenanceTask: HttpResponse<IMaintenanceTask>) => {
          if (maintenanceTask.body) {
            return of(maintenanceTask.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default maintenanceTaskResolve;
