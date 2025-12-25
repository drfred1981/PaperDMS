import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentFingerprint } from '../document-fingerprint.model';
import { DocumentFingerprintService } from '../service/document-fingerprint.service';

const documentFingerprintResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentFingerprint> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentFingerprintService)
      .find(id)
      .pipe(
        mergeMap((documentFingerprint: HttpResponse<IDocumentFingerprint>) => {
          if (documentFingerprint.body) {
            return of(documentFingerprint.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentFingerprintResolve;
