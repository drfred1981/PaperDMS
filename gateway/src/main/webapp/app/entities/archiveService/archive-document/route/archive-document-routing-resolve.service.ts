import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IArchiveDocument } from '../archive-document.model';
import { ArchiveDocumentService } from '../service/archive-document.service';

const archiveDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IArchiveDocument> => {
  const id = route.params.id;
  if (id) {
    return inject(ArchiveDocumentService)
      .find(id)
      .pipe(
        mergeMap((archiveDocument: HttpResponse<IArchiveDocument>) => {
          if (archiveDocument.body) {
            return of(archiveDocument.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default archiveDocumentResolve;
