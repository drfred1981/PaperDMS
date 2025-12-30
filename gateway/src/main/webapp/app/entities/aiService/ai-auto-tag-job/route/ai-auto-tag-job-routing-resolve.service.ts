import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAIAutoTagJob } from '../ai-auto-tag-job.model';
import { AIAutoTagJobService } from '../service/ai-auto-tag-job.service';

const aIAutoTagJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IAIAutoTagJob> => {
  const id = route.params.id;
  if (id) {
    return inject(AIAutoTagJobService)
      .find(id)
      .pipe(
        mergeMap((aIAutoTagJob: HttpResponse<IAIAutoTagJob>) => {
          if (aIAutoTagJob.body) {
            return of(aIAutoTagJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aIAutoTagJobResolve;
