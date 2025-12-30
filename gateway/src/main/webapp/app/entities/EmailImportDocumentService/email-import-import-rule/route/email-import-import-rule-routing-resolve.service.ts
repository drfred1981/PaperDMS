import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailImportImportRule } from '../email-import-import-rule.model';
import { EmailImportImportRuleService } from '../service/email-import-import-rule.service';

const emailImportImportRuleResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmailImportImportRule> => {
  const id = route.params.id;
  if (id) {
    return inject(EmailImportImportRuleService)
      .find(id)
      .pipe(
        mergeMap((emailImportImportRule: HttpResponse<IEmailImportImportRule>) => {
          if (emailImportImportRule.body) {
            return of(emailImportImportRule.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default emailImportImportRuleResolve;
