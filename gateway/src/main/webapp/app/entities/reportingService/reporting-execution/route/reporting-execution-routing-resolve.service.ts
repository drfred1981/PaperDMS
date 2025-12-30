import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingExecution } from '../reporting-execution.model';
import { ReportingExecutionService } from '../service/reporting-execution.service';

const reportingExecutionResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportingExecution> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportingExecutionService)
      .find(id)
      .pipe(
        mergeMap((reportingExecution: HttpResponse<IReportingExecution>) => {
          if (reportingExecution.body) {
            return of(reportingExecution.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportingExecutionResolve;
