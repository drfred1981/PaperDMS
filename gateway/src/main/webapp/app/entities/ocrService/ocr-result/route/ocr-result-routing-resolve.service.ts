import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOcrResult } from '../ocr-result.model';
import { OcrResultService } from '../service/ocr-result.service';

const ocrResultResolve = (route: ActivatedRouteSnapshot): Observable<null | IOcrResult> => {
  const id = route.params.id;
  if (id) {
    return inject(OcrResultService)
      .find(id)
      .pipe(
        mergeMap((ocrResult: HttpResponse<IOcrResult>) => {
          if (ocrResult.body) {
            return of(ocrResult.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ocrResultResolve;
