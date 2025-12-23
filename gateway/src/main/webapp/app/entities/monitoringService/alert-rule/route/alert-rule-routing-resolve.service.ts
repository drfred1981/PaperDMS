import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlertRule } from '../alert-rule.model';
import { AlertRuleService } from '../service/alert-rule.service';

const alertRuleResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlertRule> => {
  const id = route.params.id;
  if (id) {
    return inject(AlertRuleService)
      .find(id)
      .pipe(
        mergeMap((alertRule: HttpResponse<IAlertRule>) => {
          if (alertRule.body) {
            return of(alertRule.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default alertRuleResolve;
