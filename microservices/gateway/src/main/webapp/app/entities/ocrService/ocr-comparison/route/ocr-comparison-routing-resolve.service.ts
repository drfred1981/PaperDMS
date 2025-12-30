import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOcrComparison } from '../ocr-comparison.model';
import { OcrComparisonService } from '../service/ocr-comparison.service';

const ocrComparisonResolve = (route: ActivatedRouteSnapshot): Observable<null | IOcrComparison> => {
  const id = route.params.id;
  if (id) {
    return inject(OcrComparisonService)
      .find(id)
      .pipe(
        mergeMap((ocrComparison: HttpResponse<IOcrComparison>) => {
          if (ocrComparison.body) {
            return of(ocrComparison.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ocrComparisonResolve;
