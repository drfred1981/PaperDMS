import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScheduledReport } from '../scheduled-report.model';
import { ScheduledReportService } from '../service/scheduled-report.service';

const scheduledReportResolve = (route: ActivatedRouteSnapshot): Observable<null | IScheduledReport> => {
  const id = route.params.id;
  if (id) {
    return inject(ScheduledReportService)
      .find(id)
      .pipe(
        mergeMap((scheduledReport: HttpResponse<IScheduledReport>) => {
          if (scheduledReport.body) {
            return of(scheduledReport.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default scheduledReportResolve;
