import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScanBatch } from '../scan-batch.model';
import { ScanBatchService } from '../service/scan-batch.service';

const scanBatchResolve = (route: ActivatedRouteSnapshot): Observable<null | IScanBatch> => {
  const id = route.params.id;
  if (id) {
    return inject(ScanBatchService)
      .find(id)
      .pipe(
        mergeMap((scanBatch: HttpResponse<IScanBatch>) => {
          if (scanBatch.body) {
            return of(scanBatch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default scanBatchResolve;
