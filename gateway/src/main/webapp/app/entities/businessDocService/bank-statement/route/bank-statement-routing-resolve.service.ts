import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBankStatement } from '../bank-statement.model';
import { BankStatementService } from '../service/bank-statement.service';

const bankStatementResolve = (route: ActivatedRouteSnapshot): Observable<null | IBankStatement> => {
  const id = route.params.id;
  if (id) {
    return inject(BankStatementService)
      .find(id)
      .pipe(
        mergeMap((bankStatement: HttpResponse<IBankStatement>) => {
          if (bankStatement.body) {
            return of(bankStatement.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default bankStatementResolve;
