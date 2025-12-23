import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDashboardWidget } from '../dashboard-widget.model';
import { DashboardWidgetService } from '../service/dashboard-widget.service';

const dashboardWidgetResolve = (route: ActivatedRouteSnapshot): Observable<null | IDashboardWidget> => {
  const id = route.params.id;
  if (id) {
    return inject(DashboardWidgetService)
      .find(id)
      .pipe(
        mergeMap((dashboardWidget: HttpResponse<IDashboardWidget>) => {
          if (dashboardWidget.body) {
            return of(dashboardWidget.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default dashboardWidgetResolve;
