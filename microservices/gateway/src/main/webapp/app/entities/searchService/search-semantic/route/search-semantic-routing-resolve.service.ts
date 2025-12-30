import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISearchSemantic } from '../search-semantic.model';
import { SearchSemanticService } from '../service/search-semantic.service';

const searchSemanticResolve = (route: ActivatedRouteSnapshot): Observable<null | ISearchSemantic> => {
  const id = route.params.id;
  if (id) {
    return inject(SearchSemanticService)
      .find(id)
      .pipe(
        mergeMap((searchSemantic: HttpResponse<ISearchSemantic>) => {
          if (searchSemantic.body) {
            return of(searchSemantic.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default searchSemanticResolve;
