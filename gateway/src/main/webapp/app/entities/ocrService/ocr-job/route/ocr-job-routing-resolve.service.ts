import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOcrJob } from '../ocr-job.model';
import { OcrJobService } from '../service/ocr-job.service';

const ocrJobResolve = (route: ActivatedRouteSnapshot): Observable<null | IOcrJob> => {
  const id = route.params.id;
  if (id) {
    return inject(OcrJobService)
      .find(id)
      .pipe(
        mergeMap((ocrJob: HttpResponse<IOcrJob>) => {
          if (ocrJob.body) {
            return of(ocrJob.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ocrJobResolve;
