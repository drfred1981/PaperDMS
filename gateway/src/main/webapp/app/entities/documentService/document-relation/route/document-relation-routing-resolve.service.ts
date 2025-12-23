import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentRelation } from '../document-relation.model';
import { DocumentRelationService } from '../service/document-relation.service';

const documentRelationResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentRelation> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentRelationService)
      .find(id)
      .pipe(
        mergeMap((documentRelation: HttpResponse<IDocumentRelation>) => {
          if (documentRelation.body) {
            return of(documentRelation.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentRelationResolve;
