import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContractClause } from '../contract-clause.model';
import { ContractClauseService } from '../service/contract-clause.service';

const contractClauseResolve = (route: ActivatedRouteSnapshot): Observable<null | IContractClause> => {
  const id = route.params.id;
  if (id) {
    return inject(ContractClauseService)
      .find(id)
      .pipe(
        mergeMap((contractClause: HttpResponse<IContractClause>) => {
          if (contractClause.body) {
            return of(contractClause.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default contractClauseResolve;
