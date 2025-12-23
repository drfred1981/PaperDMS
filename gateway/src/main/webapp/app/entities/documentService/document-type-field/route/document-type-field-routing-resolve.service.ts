import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentTypeField } from '../document-type-field.model';
import { DocumentTypeFieldService } from '../service/document-type-field.service';

const documentTypeFieldResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentTypeField> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentTypeFieldService)
      .find(id)
      .pipe(
        mergeMap((documentTypeField: HttpResponse<IDocumentTypeField>) => {
          if (documentTypeField.body) {
            return of(documentTypeField.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentTypeFieldResolve;
