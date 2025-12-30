import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAICache } from '../ai-cache.model';
import { AICacheService } from '../service/ai-cache.service';

const aICacheResolve = (route: ActivatedRouteSnapshot): Observable<null | IAICache> => {
  const id = route.params.id;
  if (id) {
    return inject(AICacheService)
      .find(id)
      .pipe(
        mergeMap((aICache: HttpResponse<IAICache>) => {
          if (aICache.body) {
            return of(aICache.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aICacheResolve;
