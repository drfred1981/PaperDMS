import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRedactionJob } from '../redaction-job.model';
import { RedactionJobService } from '../service/redaction-job.service';

const redactionJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IRedactionJob> => {
  const id = route.params.id;
  if (id) {
    return inject(RedactionJobService)
      .find(id)
      .pipe(
        mergeMap((redactionJob: HttpResponse<IRedactionJob>) => {
          if (redactionJob.body) {
            return of(redactionJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default redactionJobResolve;
