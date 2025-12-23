import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDocumentProcess } from '../document-process.model';
import { DocumentProcessService } from '../service/document-process.service';

const documentProcessResolve = (route: ActivatedRouteSnapshot): Observable<null | IDocumentProcess> => {
  const id = route.params.id;
  if (id) {
    return inject(DocumentProcessService)
      .find(id)
      .pipe(
        mergeMap((documentProcess: HttpResponse<IDocumentProcess>) => {
          if (documentProcess.body) {
            return of(documentProcess.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default documentProcessResolve;
