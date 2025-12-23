import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentWatch } from '../document-watch.model';
import { DocumentWatchService } from '../service/document-watch.service';

const documentWatchResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentWatch> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentWatchService)
      .find(id)
      .pipe(
        mergeMap((documentWatch: HttpResponse<IDocumentWatch>) => {
          if (documentWatch.body) {
            return of(documentWatch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentWatchResolve;
