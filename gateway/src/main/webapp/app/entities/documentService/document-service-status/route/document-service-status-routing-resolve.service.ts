import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentServiceStatus } from '../document-service-status.model';
import { DocumentServiceStatusService } from '../service/document-service-status.service';

const documentServiceStatusResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentServiceStatus> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentServiceStatusService)
      .find(id)
      .pipe(
        mergeMap((documentServiceStatus: HttpResponse<IDocumentServiceStatus>) => {
          if (documentServiceStatus.body) {
            return of(documentServiceStatus.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentServiceStatusResolve;
