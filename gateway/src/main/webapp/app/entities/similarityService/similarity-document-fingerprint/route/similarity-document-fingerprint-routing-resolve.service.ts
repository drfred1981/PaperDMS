import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISimilarityDocumentFingerprint } from '../similarity-document-fingerprint.model';
import { SimilarityDocumentFingerprintService } from '../service/similarity-document-fingerprint.service';

const similarityDocumentFingerprintResolve = (route: ActivatedRouteSnapshot): Observable<null | ISimilarityDocumentFingerprint> => {
  const id = route.params.id;
  if (id) {
    return inject(SimilarityDocumentFingerprintService)
      .find(id)
      .pipe(
        mergeMap((similarityDocumentFingerprint: HttpResponse<ISimilarityDocumentFingerprint>) => {
          if (similarityDocumentFingerprint.body) {
            return of(similarityDocumentFingerprint.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default similarityDocumentFingerprintResolve;
