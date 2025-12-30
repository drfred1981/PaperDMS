import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingScheduledReport } from '../reporting-scheduled-report.model';
import { ReportingScheduledReportService } from '../service/reporting-scheduled-report.service';

const reportingScheduledReportResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportingScheduledReport> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportingScheduledReportService)
      .find(id)
      .pipe(
        mergeMap((reportingScheduledReport: HttpResponse<IReportingScheduledReport>) => {
          if (reportingScheduledReport.body) {
            return of(reportingScheduledReport.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportingScheduledReportResolve;
