import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOcrCache } from '../ocr-cache.model';
import { OcrCacheService } from '../service/ocr-cache.service';

const ocrCacheResolve = (route: ActivatedRouteSnapshot): Observable<null | IOcrCache> => {
  const id = route.params.id;
  if (id) {
    return inject(OcrCacheService)
      .find(id)
      .pipe(
        mergeMap((ocrCache: HttpResponse<IOcrCache>) => {
          if (ocrCache.body) {
            return of(ocrCache.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default ocrCacheResolve;
