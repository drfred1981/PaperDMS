import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExportJob } from '../export-job.model';
import { ExportJobService } from '../service/export-job.service';

const exportJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IExportJob> => {
  const id = route.params.id;
  if (id) {
    return inject(ExportJobService)
      .find(id)
      .pipe(
        mergeMap((exportJob: HttpResponse<IExportJob>) => {
          if (exportJob.body) {
            return of(exportJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default exportJobResolve;
