import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExportResult } from '../export-result.model';
import { ExportResultService } from '../service/export-result.service';

const exportResultResolve = (route: ActivatedRouteSnapshot): Observable<null | IExportResult> => {
  const id = route.params.id;
  if (id) {
    return inject(ExportResultService)
      .find(id)
      .pipe(
        mergeMap((exportResult: HttpResponse<IExportResult>) => {
          if (exportResult.body) {
            return of(exportResult.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default exportResultResolve;
