import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMonitoringAlertRule } from '../monitoring-alert-rule.model';
import { MonitoringAlertRuleService } from '../service/monitoring-alert-rule.service';

const monitoringAlertRuleResolve = (route: ActivatedRouteSnapshot): Observable<null | IMonitoringAlertRule> => {
  const id = route.params.id;
  if (id) {
    return inject(MonitoringAlertRuleService)
      .find(id)
      .pipe(
        mergeMap((monitoringAlertRule: HttpResponse<IMonitoringAlertRule>) => {
          if (monitoringAlertRule.body) {
            return of(monitoringAlertRule.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default monitoringAlertRuleResolve;
