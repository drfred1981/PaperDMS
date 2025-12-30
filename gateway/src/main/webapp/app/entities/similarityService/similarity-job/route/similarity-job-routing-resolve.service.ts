import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISimilarityJob } from '../similarity-job.model';
import { SimilarityJobService } from '../service/similarity-job.service';

const similarityJobResolve = (route: ActivatedRouteSnapshot): Observable<null | ISimilarityJob> => {
  const id = route.params.id;
  if (id) {
    return inject(SimilarityJobService)
      .find(id)
      .pipe(
        mergeMap((similarityJob: HttpResponse<ISimilarityJob>) => {
          if (similarityJob.body) {
            return of(similarityJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default similarityJobResolve;
