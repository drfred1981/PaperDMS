import { HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Router } from '@angular/router';

import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManualChapter } from '../manual-chapter.model';
import { ManualChapterService } from '../service/manual-chapter.service';

const manualChapterResolve = (route: ActivatedRouteSnapshot): Observable<null | IManualChapter> => {
  const id = route.params.id;
  if (id) {
    return inject(ManualChapterService)
      .find(id)
      .pipe(
        mergeMap((manualChapter: HttpResponse<IManualChapter>) => {
          if (manualChapter.body) {
            return of(manualChapter.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default manualChapterResolve;
