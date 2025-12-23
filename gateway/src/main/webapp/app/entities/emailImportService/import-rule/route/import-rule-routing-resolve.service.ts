import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IImportRule } from '../import-rule.model';
import { ImportRuleService } from '../service/import-rule.service';

const importRuleResolve = (route: ActivatedRouteSnapshot): Observable<null | IImportRule> => {
  const id = route.params.id;
  if (id) {
    return inject(ImportRuleService)
      .find(id)
      .pipe(
        mergeMap((importRule: HttpResponse<IImportRule>) => {
          if (importRule.body) {
            return of(importRule.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default importRuleResolve;
