import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { SystemHealthService } from '../service/system-health.service';
import { ISystemHealth } from '../system-health.model';

const systemHealthResolve = (route: ActivatedRouteSnapshot): Observable<null | ISystemHealth> => {
  const id = route.params.id;
  if (id) {
    return inject(SystemHealthService)
      .find(id)
      .pipe(
        mergeMap((systemHealth: HttpResponse<ISystemHealth>) => {
          if (systemHealth.body) {
            return of(systemHealth.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default systemHealthResolve;
