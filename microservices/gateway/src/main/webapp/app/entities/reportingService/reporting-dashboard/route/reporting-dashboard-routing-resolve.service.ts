import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingDashboard } from '../reporting-dashboard.model';
import { ReportingDashboardService } from '../service/reporting-dashboard.service';

const reportingDashboardResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportingDashboard> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportingDashboardService)
      .find(id)
      .pipe(
        mergeMap((reportingDashboard: HttpResponse<IReportingDashboard>) => {
          if (reportingDashboard.body) {
            return of(reportingDashboard.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportingDashboardResolve;
