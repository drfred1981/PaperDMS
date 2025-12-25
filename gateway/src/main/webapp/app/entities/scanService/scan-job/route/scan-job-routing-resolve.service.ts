import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScanJob } from '../scan-job.model';
import { ScanJobService } from '../service/scan-job.service';

const scanJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IScanJob> => {
  const id = route.params.id;
  if (id) {
    return inject(ScanJobService)
      .find(id)
      .pipe(
        mergeMap((scanJob: HttpResponse<IScanJob>) => {
          if (scanJob.body) {
            return of(scanJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default scanJobResolve;
