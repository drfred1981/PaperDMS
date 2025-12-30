import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransformRedactionJob } from '../transform-redaction-job.model';
import { TransformRedactionJobService } from '../service/transform-redaction-job.service';

const transformRedactionJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransformRedactionJob> => {
  const id = route.params.id;
  if (id) {
    return inject(TransformRedactionJobService)
      .find(id)
      .pipe(
        mergeMap((transformRedactionJob: HttpResponse<ITransformRedactionJob>) => {
          if (transformRedactionJob.body) {
            return of(transformRedactionJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default transformRedactionJobResolve;
