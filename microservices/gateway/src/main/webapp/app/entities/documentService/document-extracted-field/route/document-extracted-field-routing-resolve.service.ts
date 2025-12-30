import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentExtractedField } from '../document-extracted-field.model';
import { DocumentExtractedFieldService } from '../service/document-extracted-field.service';

const documentExtractedFieldResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentExtractedField> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentExtractedFieldService)
      .find(id)
      .pipe(
        mergeMap((documentExtractedField: HttpResponse<IDocumentExtractedField>) => {
          if (documentExtractedField.body) {
            return of(documentExtractedField.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentExtractedFieldResolve;
