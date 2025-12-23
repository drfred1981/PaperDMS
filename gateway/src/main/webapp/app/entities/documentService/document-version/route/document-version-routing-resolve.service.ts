import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentVersion } from '../document-version.model';
import { DocumentVersionService } from '../service/document-version.service';

const documentVersionResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentVersion> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentVersionService)
      .find(id)
      .pipe(
        mergeMap((documentVersion: HttpResponse<IDocumentVersion>) => {
          if (documentVersion.body) {
            return of(documentVersion.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentVersionResolve;
