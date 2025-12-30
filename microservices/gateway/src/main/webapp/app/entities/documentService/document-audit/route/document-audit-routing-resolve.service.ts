import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentAudit } from '../document-audit.model';
import { DocumentAuditService } from '../service/document-audit.service';

const documentAuditResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentAudit> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentAuditService)
      .find(id)
      .pipe(
        mergeMap((documentAudit: HttpResponse<IDocumentAudit>) => {
          if (documentAudit.body) {
            return of(documentAudit.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentAuditResolve;
