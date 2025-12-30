import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransformConversionJob } from '../transform-conversion-job.model';
import { TransformConversionJobService } from '../service/transform-conversion-job.service';

const transformConversionJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransformConversionJob> => {
  const id = route.params.id;
  if (id) {
    return inject(TransformConversionJobService)
      .find(id)
      .pipe(
        mergeMap((transformConversionJob: HttpResponse<ITransformConversionJob>) => {
          if (transformConversionJob.body) {
            return of(transformConversionJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transformConversionJobResolve;
