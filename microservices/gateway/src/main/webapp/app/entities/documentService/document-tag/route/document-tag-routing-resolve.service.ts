import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentTag } from '../document-tag.model';
import { DocumentTagService } from '../service/document-tag.service';

const documentTagResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentTag> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentTagService)
      .find(id)
      .pipe(
        mergeMap((documentTag: HttpResponse<IDocumentTag>) => {
          if (documentTag.body) {
            return of(documentTag.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentTagResolve;
