import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompressionJob } from '../compression-job.model';
import { CompressionJobService } from '../service/compression-job.service';

const compressionJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompressionJob> => {
  const id = route.params.id;
  if (id) {
    return inject(CompressionJobService)
      .find(id)
      .pipe(
        mergeMap((compressionJob: HttpResponse<ICompressionJob>) => {
          if (compressionJob.body) {
            return of(compressionJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default compressionJobResolve;
