import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentMetadata } from '../document-metadata.model';
import { DocumentMetadataService } from '../service/document-metadata.service';

const documentMetadataResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentMetadata> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentMetadataService)
      .find(id)
      .pipe(
        mergeMap((documentMetadata: HttpResponse<IDocumentMetadata>) => {
          if (documentMetadata.body) {
            return of(documentMetadata.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentMetadataResolve;
