import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISimilarityCluster } from '../similarity-cluster.model';
import { SimilarityClusterService } from '../service/similarity-cluster.service';

const similarityClusterResolve = (route: ActivatedRouteSnapshot): Observable<null | ISimilarityCluster> => {
  const id = route.params.id;
  if (id) {
    return inject(SimilarityClusterService)
      .find(id)
      .pipe(
        mergeMap((similarityCluster: HttpResponse<ISimilarityCluster>) => {
          if (similarityCluster.body) {
            return of(similarityCluster.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default similarityClusterResolve;
