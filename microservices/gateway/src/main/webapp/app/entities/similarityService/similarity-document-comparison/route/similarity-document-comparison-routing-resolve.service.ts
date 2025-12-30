import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISimilarityDocumentComparison } from '../similarity-document-comparison.model';
import { SimilarityDocumentComparisonService } from '../service/similarity-document-comparison.service';

const similarityDocumentComparisonResolve = (route: ActivatedRouteSnapshot): Observable<null | ISimilarityDocumentComparison> => {
  const id = route.params.id;
  if (id) {
    return inject(SimilarityDocumentComparisonService)
      .find(id)
      .pipe(
        mergeMap((similarityDocumentComparison: HttpResponse<ISimilarityDocumentComparison>) => {
          if (similarityDocumentComparison.body) {
            return of(similarityDocumentComparison.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default similarityDocumentComparisonResolve;
