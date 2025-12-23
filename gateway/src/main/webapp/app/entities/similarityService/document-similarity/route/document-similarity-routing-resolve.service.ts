import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentSimilarity } from '../document-similarity.model';
import { DocumentSimilarityService } from '../service/document-similarity.service';

const documentSimilarityResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentSimilarity> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentSimilarityService)
      .find(id)
      .pipe(
        mergeMap((documentSimilarity: HttpResponse<IDocumentSimilarity>) => {
          if (documentSimilarity.body) {
            return of(documentSimilarity.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentSimilarityResolve;
