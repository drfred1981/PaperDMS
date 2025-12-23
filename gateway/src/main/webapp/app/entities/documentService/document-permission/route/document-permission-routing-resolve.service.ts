import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentPermission } from '../document-permission.model';
import { DocumentPermissionService } from '../service/document-permission.service';

const documentPermissionResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentPermission> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentPermissionService)
      .find(id)
      .pipe(
        mergeMap((documentPermission: HttpResponse<IDocumentPermission>) => {
          if (documentPermission.body) {
            return of(documentPermission.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentPermissionResolve;
