import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPerformanceMetric } from '../performance-metric.model';
import { PerformanceMetricService } from '../service/performance-metric.service';

const performanceMetricResolve = (route: ActivatedRouteSnapshot): Observable<null | IPerformanceMetric> => {
  const id = route.params.id;
  if (id) {
    return inject(PerformanceMetricService)
      .find(id)
      .pipe(
        mergeMap((performanceMetric: HttpResponse<IPerformanceMetric>) => {
          if (performanceMetric.body) {
            return of(performanceMetric.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default performanceMetricResolve;
