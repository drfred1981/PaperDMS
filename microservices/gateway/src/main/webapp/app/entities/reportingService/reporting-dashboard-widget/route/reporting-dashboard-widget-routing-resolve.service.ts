import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReportingDashboardWidget } from '../reporting-dashboard-widget.model';
import { ReportingDashboardWidgetService } from '../service/reporting-dashboard-widget.service';

const reportingDashboardWidgetResolve = (route: ActivatedRouteSnapshot): Observable<null | IReportingDashboardWidget> => {
  const id = route.params.id;
  if (id) {
    return inject(ReportingDashboardWidgetService)
      .find(id)
      .pipe(
        mergeMap((reportingDashboardWidget: HttpResponse<IReportingDashboardWidget>) => {
          if (reportingDashboardWidget.body) {
            return of(reportingDashboardWidget.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default reportingDashboardWidgetResolve;
