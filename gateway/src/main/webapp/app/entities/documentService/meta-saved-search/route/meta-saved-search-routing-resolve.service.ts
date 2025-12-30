import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMetaSavedSearch } from '../meta-saved-search.model';
import { MetaSavedSearchService } from '../service/meta-saved-search.service';

const metaSavedSearchResolve = (route: ActivatedRouteSnapshot): Observable<null | IMetaSavedSearch> => {
  const id = route.params.id;
  if (id) {
    return inject(MetaSavedSearchService)
      .find(id)
      .pipe(
        mergeMap((metaSavedSearch: HttpResponse<IMetaSavedSearch>) => {
          if (metaSavedSearch.body) {
            return of(metaSavedSearch.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default metaSavedSearchResolve;
