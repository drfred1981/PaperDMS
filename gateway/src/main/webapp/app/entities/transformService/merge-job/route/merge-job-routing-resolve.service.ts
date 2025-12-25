import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMergeJob } from '../merge-job.model';
import { MergeJobService } from '../service/merge-job.service';

const mergeJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IMergeJob> => {
  const id = route.params.id;
  if (id) {
    return inject(MergeJobService)
      .find(id)
      .pipe(
        mergeMap((mergeJob: HttpResponse<IMergeJob>) => {
          if (mergeJob.body) {
            return of(mergeJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default mergeJobResolve;
