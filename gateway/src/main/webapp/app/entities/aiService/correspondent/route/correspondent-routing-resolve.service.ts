import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorrespondent } from '../correspondent.model';
import { CorrespondentService } from '../service/correspondent.service';

const correspondentResolve = (route: ActivatedRouteSnapshot): Observable<null | ICorrespondent> => {
  const id = route.params.id;
  if (id) {
    return inject(CorrespondentService)
      .find(id)
      .pipe(
        mergeMap((correspondent: HttpResponse<ICorrespondent>) => {
          if (correspondent.body) {
            return of(correspondent.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default correspondentResolve;
