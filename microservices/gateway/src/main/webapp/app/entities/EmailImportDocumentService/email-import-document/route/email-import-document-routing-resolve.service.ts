import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailImportDocument } from '../email-import-document.model';
import { EmailImportDocumentService } from '../service/email-import-document.service';

const emailImportDocumentResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmailImportDocument> => {
  const id = route.params.id;
  if (id) {
    return inject(EmailImportDocumentService)
      .find(id)
      .pipe(
        mergeMap((emailImportDocument: HttpResponse<IEmailImportDocument>) => {
          if (emailImportDocument.body) {
            return of(emailImportDocument.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default emailImportDocumentResolve;
