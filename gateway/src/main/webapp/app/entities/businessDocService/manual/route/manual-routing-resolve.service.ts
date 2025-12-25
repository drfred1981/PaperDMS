import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManual } from '../manual.model';
import { ManualService } from '../service/manual.service';

const manualResolve = (route: ActivatedRouteSnapshot): Observable<null | IManual> => {
  const id = route.params.id;
  if (id) {
    return inject(ManualService)
      .find(id)
      .pipe(
        mergeMap((manual: HttpResponse<IManual>) => {
          if (manual.body) {
            return of(manual.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default manualResolve;
