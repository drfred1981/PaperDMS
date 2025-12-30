import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingSystemMetric } from '../reporting-system-metric.model';
import { ReportingSystemMetricService } from '../service/reporting-system-metric.service';

const reportingSystemMetricResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportingSystemMetric> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportingSystemMetricService)
      .find(id)
      .pipe(
        mergeMap((reportingSystemMetric: HttpResponse<IReportingSystemMetric>) => {
          if (reportingSystemMetric.body) {
            return of(reportingSystemMetric.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportingSystemMetricResolve;
