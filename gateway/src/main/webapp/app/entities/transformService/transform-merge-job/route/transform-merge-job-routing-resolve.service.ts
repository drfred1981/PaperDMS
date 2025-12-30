import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransformMergeJob } from '../transform-merge-job.model';
import { TransformMergeJobService } from '../service/transform-merge-job.service';

const transformMergeJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransformMergeJob> => {
  const id = route.params.id;
  if (id) {
    return inject(TransformMergeJobService)
      .find(id)
      .pipe(
        mergeMap((transformMergeJob: HttpResponse<ITransformMergeJob>) => {
          if (transformMergeJob.body) {
            return of(transformMergeJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transformMergeJobResolve;
