import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransformWatermarkJob } from '../transform-watermark-job.model';
import { TransformWatermarkJobService } from '../service/transform-watermark-job.service';

const transformWatermarkJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransformWatermarkJob> => {
  const id = route.params.id;
  if (id) {
    return inject(TransformWatermarkJobService)
      .find(id)
      .pipe(
        mergeMap((transformWatermarkJob: HttpResponse<ITransformWatermarkJob>) => {
          if (transformWatermarkJob.body) {
            return of(transformWatermarkJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transformWatermarkJobResolve;
