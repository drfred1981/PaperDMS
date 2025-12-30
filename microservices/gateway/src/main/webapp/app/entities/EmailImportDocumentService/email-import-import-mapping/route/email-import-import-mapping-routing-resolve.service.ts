import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmailImportImportMapping } from '../email-import-import-mapping.model';
import { EmailImportImportMappingService } from '../service/email-import-import-mapping.service';

const emailImportImportMappingResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmailImportImportMapping> => {
  const id = route.params.id;
  if (id) {
    return inject(EmailImportImportMappingService)
      .find(id)
      .pipe(
        mergeMap((emailImportImportMapping: HttpResponse<IEmailImportImportMapping>) => {
          if (emailImportImportMapping.body) {
            return of(emailImportImportMapping.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default emailImportImportMappingResolve;
