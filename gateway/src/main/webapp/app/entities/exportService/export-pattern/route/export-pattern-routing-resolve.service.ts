import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExportPattern } from '../export-pattern.model';
import { ExportPatternService } from '../service/export-pattern.service';

const exportPatternResolve = (route: ActivatedRouteSnapshot): Observable<null | IExportPattern> => {
  const id = route.params.id;
  if (id) {
    return inject(ExportPatternService)
      .find(id)
      .pipe(
        mergeMap((exportPattern: HttpResponse<IExportPattern>) => {
          if (exportPattern.body) {
            return of(exportPattern.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default exportPatternResolve;
