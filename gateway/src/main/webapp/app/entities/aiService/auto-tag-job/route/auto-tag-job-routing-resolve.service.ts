import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAutoTagJob } from '../auto-tag-job.model';
import { AutoTagJobService } from '../service/auto-tag-job.service';

const autoTagJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IAutoTagJob> => {
  const id = route.params.id;
  if (id) {
    return inject(AutoTagJobService)
      .find(id)
      .pipe(
        mergeMap((autoTagJob: HttpResponse<IAutoTagJob>) => {
          if (autoTagJob.body) {
            return of(autoTagJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default autoTagJobResolve;
