import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAiCache } from '../ai-cache.model';
import { AiCacheService } from '../service/ai-cache.service';

const aiCacheResolve = (route: ActivatedRouteSnapshot): Observable<null | IAiCache> => {
  const id = route.params.id;
  if (id) {
    return inject(AiCacheService)
      .find(id)
      .pipe(
        mergeMap((aiCache: HttpResponse<IAiCache>) => {
          if (aiCache.body) {
            return of(aiCache.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default aiCacheResolve;
