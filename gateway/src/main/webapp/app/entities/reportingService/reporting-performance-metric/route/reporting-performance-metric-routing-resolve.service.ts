import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingPerformanceMetric } from '../reporting-performance-metric.model';
import { ReportingPerformanceMetricService } from '../service/reporting-performance-metric.service';

const reportingPerformanceMetricResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportingPerformanceMetric> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportingPerformanceMetricService)
      .find(id)
      .pipe(
        mergeMap((reportingPerformanceMetric: HttpResponse<IReportingPerformanceMetric>) => {
          if (reportingPerformanceMetric.body) {
            return of(reportingPerformanceMetric.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportingPerformanceMetricResolve;
