import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemMetric } from '../system-metric.model';
import { SystemMetricService } from '../service/system-metric.service';

const systemMetricResolve = (route: ActivatedRouteSnapshot): Observable<null | ISystemMetric> => {
  const id = route.params.id;
  if (id) {
    return inject(SystemMetricService)
      .find(id)
      .pipe(
        mergeMap((systemMetric: HttpResponse<ISystemMetric>) => {
          if (systemMetric.body) {
            return of(systemMetric.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default systemMetricResolve;
