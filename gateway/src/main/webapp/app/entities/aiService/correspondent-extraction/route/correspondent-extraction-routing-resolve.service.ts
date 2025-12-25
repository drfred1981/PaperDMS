import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICorrespondentExtraction } from '../correspondent-extraction.model';
import { CorrespondentExtractionService } from '../service/correspondent-extraction.service';

const correspondentExtractionResolve = (route: ActivatedRouteSnapshot): Observable<null | ICorrespondentExtraction> => {
  const id = route.params.id;
  if (id) {
    return inject(CorrespondentExtractionService)
      .find(id)
      .pipe(
        mergeMap((correspondentExtraction: HttpResponse<ICorrespondentExtraction>) => {
          if (correspondentExtraction.body) {
            return of(correspondentExtraction.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default correspondentExtractionResolve;
