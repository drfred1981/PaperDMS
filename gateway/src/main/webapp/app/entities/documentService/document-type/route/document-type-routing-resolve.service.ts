import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentType } from '../document-type.model';
import { DocumentTypeService } from '../service/document-type.service';

const documentTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentType> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentTypeService)
      .find(id)
      .pipe(
        mergeMap((documentType: HttpResponse<IDocumentType>) => {
          if (documentType.body) {
            return of(documentType.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentTypeResolve;
