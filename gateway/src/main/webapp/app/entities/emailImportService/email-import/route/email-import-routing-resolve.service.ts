import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailImport } from '../email-import.model';
import { EmailImportService } from '../service/email-import.service';

const emailImportResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmailImport> => {
  const id = route.params.id;
  if (id) {
    return inject(EmailImportService)
      .find(id)
      .pipe(
        mergeMap((emailImport: HttpResponse<IEmailImport>) => {
          if (emailImport.body) {
            return of(emailImport.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default emailImportResolve;
