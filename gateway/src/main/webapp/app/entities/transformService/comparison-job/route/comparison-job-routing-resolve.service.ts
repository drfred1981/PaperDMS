import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComparisonJob } from '../comparison-job.model';
import { ComparisonJobService } from '../service/comparison-job.service';

const comparisonJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IComparisonJob> => {
  const id = route.params.id;
  if (id) {
    return inject(ComparisonJobService)
      .find(id)
      .pipe(
        mergeMap((comparisonJob: HttpResponse<IComparisonJob>) => {
          if (comparisonJob.body) {
            return of(comparisonJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default comparisonJobResolve;
