import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransformCompressionJob } from '../transform-compression-job.model';
import { TransformCompressionJobService } from '../service/transform-compression-job.service';

const transformCompressionJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransformCompressionJob> => {
  const id = route.params.id;
  if (id) {
    return inject(TransformCompressionJobService)
      .find(id)
      .pipe(
        mergeMap((transformCompressionJob: HttpResponse<ITransformCompressionJob>) => {
          if (transformCompressionJob.body) {
            return of(transformCompressionJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transformCompressionJobResolve;
